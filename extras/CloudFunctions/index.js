'use strict';

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);


exports.sendMessageNotification = functions.database.ref('/notification_message/{userRecipient}/{userSender}').onWrite(event => {
  const userRecipient = event.params.userRecipient;
  const userSender = event.params.userSender;

  if (!event.data.val()) {
    return console.log('User ', userSender, ' did not send ', userRecipient);
  }
  console.log('We have a new message UID:', userSender, 'for user:', userRecipient);


  const getDeviceTokensPromise = admin.database().ref(`/users/${userRecipient}/notificationToken/token`).once('value');

  const getUserSenderUid = admin.database().ref(`/users/${userSender}/userId`).once('value');

  const getSenderProfilePromise = admin.auth().getUser(getUserSenderUid);

  return Promise.all([getDeviceTokensPromise, getFollowerProfilePromise]).then(results => {
    const tokensSnapshot = results[0];
    const sender = results[1];

    if (!tokensSnapshot.hasChildren()) {
      return console.log('There are no notification tokens to send to.');
    }
    console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');
    console.log('Fetched sender profile', sender);

    const payload = {
      notification: {
        title: 'You have a new message!',
        body: `${sender.displayName} sent a message.`,
        icon: sender.photoURL
      }
    };

    const tokens = Object.keys(tokensSnapshot.val());

    return admin.messaging().sendToDevice(tokens, payload).then(response => {

      const tokensToRemove = [];
      response.results.forEach((result, index) => {
        const error = result.error;
        if (error) {
          console.error('Failure sending notification to', tokens[index], error);
          
          if (error.code === 'messaging/invalid-registration-token' ||
              error.code === 'messaging/registration-token-not-registered') {
            tokensToRemove.push(tokensSnapshot.ref.child(tokens[index]).remove());
          }
        }
      });
      return Promise.all(tokensToRemove);
    });
  });
});
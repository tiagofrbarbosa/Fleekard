package io.github.tiagofrbarbosa.fleekard.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.tiagofrbarbosa.fleekard.R;
import io.github.tiagofrbarbosa.fleekard.adapter.ChatAdapter;
import io.github.tiagofrbarbosa.fleekard.model.Chat;
import timber.log.Timber;

/**
 * Created by tfbarbosa on 16/09/17.
 */

public class FragmentChat extends Fragment {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    protected ChatAdapter adapter;
    protected List<Chat> chats;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        chats = Chat.getChats();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter = new ChatAdapter(getActivity(), chats, onClickChat()));
    }

    protected ChatAdapter.ChatOnclickListener onClickChat(){

        return new ChatAdapter.ChatOnclickListener(){

            @Override
            public void onClickChat(ChatAdapter.ChatsViewHolder holder, int idx) {
                Chat c = chats.get(idx);
                Timber.i(String.valueOf(c.userName));
            }
        };
    }
}

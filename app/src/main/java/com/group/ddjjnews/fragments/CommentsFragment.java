package com.group.ddjjnews.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.group.ddjjnews.adapters.GenericAdapter;
import com.group.ddjjnews.databinding.CommentItemBinding;
import com.group.ddjjnews.databinding.FragmentCommentsBinding;
import com.group.ddjjnews.models.Comment;
import com.group.ddjjnews.models.News;
import com.parse.FunctionCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class CommentsFragment extends DialogFragment {
    FragmentCommentsBinding binding;
    GenericAdapter<Comment, CommentItemBinding> adapter;
    ArrayList<Comment> items = new ArrayList<>();

    private String newsId;

    public CommentsFragment() {}

    public static CommentsFragment newInstance(String newsId) {
        CommentsFragment fragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putString("newsId", newsId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new GenericAdapter<Comment, CommentItemBinding>(getContext(), items, CommentItemBinding.class) {
            @Override
            public void bindItem(CommentItemBinding binding, Comment item, int position) {
                binding.tvText.setText(item.getKeyText());
                binding.tvUserEmail.setText(item.getKeyUser().getUsername());
            }
        };

        if (getArguments() != null) {
            newsId = getArguments().getString("newsId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCommentsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.rcComments.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcComments.setAdapter(adapter);

        binding.btnCommentSend.setOnClickListener(view1 -> {
            String commentText = binding.edMessage.getText().toString();
            newComment(commentText);
        });

        getLastComment();
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }

    private void getLastComment() {
        Comment.getAllFor(newsId, (objects, e) -> {
            if (e == null) {
                items.clear();
                for (Object obj: objects) {
                    items.add((Comment) obj);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void newComment(String text) {
        if (text == null) return;
        Comment.createFor(newsId, text, (object, e) -> {
            if (e == null) {
                Toast.makeText(getContext(), "Message added!", Toast.LENGTH_SHORT).show();
                binding.edMessage.setText("");
            } else {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
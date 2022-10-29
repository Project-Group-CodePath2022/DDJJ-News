package com.group.ddjjnews.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.group.ddjjnews.Utils.IndeterminateDialog;
import com.group.ddjjnews.adapters.GenericAdapter;
import com.group.ddjjnews.databinding.CommentItemBinding;
import com.group.ddjjnews.databinding.EmptyStateBinding;
import com.group.ddjjnews.databinding.FragmentCommentsBinding;
import com.group.ddjjnews.models.Comment;

import java.util.ArrayList;
import java.util.Objects;

public class CommentsFragment extends DialogFragment {
    FragmentCommentsBinding binding;
    GenericAdapter<Comment, CommentItemBinding, EmptyStateBinding> adapter;
    ArrayList<Comment> items = new ArrayList<>();
    private String newsId;
    Context context;

    public CommentsFragment() {}

    public static CommentsFragment newInstance(String newsId) {
        CommentsFragment fragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putString("newsId", newsId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new GenericAdapter<Comment, CommentItemBinding, EmptyStateBinding>(getContext(), items, CommentItemBinding.class, EmptyStateBinding.class) {
            @SuppressLint("SetTextI18n")
            @Override
            public void bindItem(CommentItemBinding binding, Comment item, int position) {
                binding.tvText.setText(item.getKeyText());
                binding.tvUserEmail.setText(item.getKeyUser().getUsername() + "( " + item.getCreatedAt().toGMTString() + " )");
            }

            @Override
            public void bindEmpty(EmptyStateBinding binding) {
                binding.title.setText("No comments");
            }
        };
        if (getArguments() != null) {
            newsId = getArguments().getString("newsId");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
        WindowManager.LayoutParams params = Objects.requireNonNull(getDialog()).getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(params);
        // Call super onResume after sizing
        super.onResume();
    }

    private void getLastComment() {
        Comment.getAllFor(newsId, (objects, e) -> {
            Toast.makeText(this.context, "Comments!", Toast.LENGTH_SHORT).show();

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
        IndeterminateDialog d = IndeterminateDialog.newInstance("Sending comment...", "please wait!");
        d.show(getChildFragmentManager(), "send_comment");
        Comment.createFor(newsId, text, (object, e) -> {
            if (e == null) {
                Toast.makeText(this.context, "Message added!", Toast.LENGTH_SHORT).show();
                items.add((Comment) object);
                adapter.notifyDataSetChanged();
                binding.edMessage.setText("");
            } else {
                 Toast.makeText(this.context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            d.dismiss();
        });
    }
}
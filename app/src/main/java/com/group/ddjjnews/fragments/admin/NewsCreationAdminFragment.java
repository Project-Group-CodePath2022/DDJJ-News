package com.group.ddjjnews.fragments.admin;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.group.ddjjnews.databinding.FragmentNewsCreationAdminBinding;
import com.group.ddjjnews.models.News;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class NewsCreationAdminFragment extends DialogFragment {
    FragmentNewsCreationAdminBinding binding;
    private static final int PICK_PHOTO = 1000;
    public static final String KEY_NEWS = "news";
    public static final String KEY_POSITION = "position";
    int position;
    News news;
    ArrayAdapter<String> adapter;
    String category;
    private List<String> nameCategories = new ArrayList<>();
    byte[] bytesFileImage;

    public NewsCreationAdminFragment() {}

    public static NewsCreationAdminFragment newInstance(News news, int pos) {
        NewsCreationAdminFragment fragment = new NewsCreationAdminFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_NEWS, news);
        args.putInt(KEY_POSITION, pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.news = getArguments().getParcelable(KEY_NEWS);
            this.position = getArguments().getInt(KEY_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewsCreationAdminBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setSpinnerCategory();

        prefillIpUpdate();

        binding.takeImg.setOnClickListener(view12 -> takeImage(PICK_PHOTO));

        binding.btnCreateNews.setOnClickListener(view1 -> {
            if (!fieldsOk()) return ;
            if (news != null)
                updateNews();
            else
                createNew();
        });
    }

    @Override
    public void onResume() {
        WindowManager.LayoutParams params = Objects.requireNonNull(getDialog()).getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(params);
        super.onResume();
    }

    private void prefillIpUpdate() {
        if (news != null) {
            binding.edTitle.setText(news.getKeyTitle());
            binding.edContent.setText(news.getKeyContent());
            binding.categorySpinner.setSelection(adapter.getPosition(news.getKeyCategory().getString("name")));
            try{
                Glide.with(getContext()).load(news.getKeyImage().getUrl()).into(binding.takeImg);
            }catch (Exception e){e.printStackTrace();}
            binding.btnCreateNews.setText("Update");
        }
    }

    private void updateNews() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("title", binding.edTitle.getText().toString());
        params.put("description", binding.edDesc.getText().toString());
        params.put("content", binding.edContent.getText().toString());
        params.put("file", bytesFileImage);
        params.put("category", category);
        params.put("newsId", news.getObjectId());


        News.updateNewsAdmin(params, (object, e) -> {
            if (e == null) {
                this.dismiss();
                ((NewsListAdminFragment)getParentFragment()).updateItem((News) object, position);
                Toast.makeText(getContext(), "Successfully saved!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createNew() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("title", binding.edTitle.getText().toString());
        params.put("description", binding.edDesc.getText().toString());
        params.put("content", binding.edContent.getText().toString());
        params.put("file", bytesFileImage);
        params.put("category", category);

        News.createNewsAdmin(params, (object, e) -> {
            if (e == null) {
                this.dismiss();
                ((NewsListAdminFragment)getParentFragment()).addItem((News) object);
                Toast.makeText(getContext(), "Successfully saved!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean fieldsOk() {
        if (
            binding.edTitle.getText().toString().trim().isEmpty() ||
            binding.edContent.getText().toString().trim().isEmpty() ||
            binding.edDesc.getText().toString().trim().isEmpty() ||
            category == null ||
            binding.takeImg.getDrawable() == null
        ) {
            Toast.makeText(getContext(), "Required fields!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_PHOTO && resultCode == RESULT_OK) {
            Uri image = data.getData();
            binding.takeImg.setImageURI(image);
            Bitmap bm = ((BitmapDrawable)binding.takeImg.getDrawable()).getBitmap();
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
            bytesFileImage = bytes.toByteArray();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void takeImage(int request_code) {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(i, request_code);
    }

    private void setSpinnerCategory() {
        // Setup categories for news
        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, nameCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.categorySpinner.setAdapter(adapter);
        binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = (String) adapterView.getItemAtPosition(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    public void setNameCategories(List<String> nameCategories) { this.nameCategories = nameCategories; }
}
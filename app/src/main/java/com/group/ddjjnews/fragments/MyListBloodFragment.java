package com.group.ddjjnews.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.group.ddjjnews.R;
import com.group.ddjjnews.adapters.MyBloodAdapter;
import com.group.ddjjnews.models.Blood;

import java.util.Collection;
import java.util.HashMap;


public class MyListBloodFragment extends ListBloodFragment {
    public static MyListBloodFragment newInstance() {
        MyListBloodFragment f = new MyListBloodFragment();
        Bundle bundle = new Bundle();
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.adapter = new MyBloodAdapter(getContext(), requests);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MyBloodAdapter)this.adapter).setListener(new MyBloodAdapter.BloodListener() {
            @Override
            public void onOptionsItemClicked(Blood item, int pos) {
                showBSD(item, pos);
            }
        });
    }

    @Override
    protected void getRequestedBlood() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("me", true);
        Blood.getAll(params, new Blood.Callback() {
            @Override
            public void done(Blood object, Exception e) {}
            @Override
            public void done(Collection objects, Exception e) {
                if (e == null) {
                    requests.clear();
                    requests.addAll(objects);
                    adapter.notifyDataSetChanged();
                    sRefresh.setRefreshing(false);
                }
            }
        });
    }

    private void showBSD(Blood item, int pos) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(R.layout.bsd_my_blood);
        bottomSheetDialog.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Blood.deleteBlood(item.getObjectId(), new Blood.Callback() {
                    @Override
                    public void done(Blood object, Exception e) {
                        if (e == null) {
                            requests.remove(pos);
                            adapter.notifyItemRemoved(pos);
                            Toast.makeText(getContext(), "Successfully deleted !", Toast.LENGTH_SHORT).show();
                            bottomSheetDialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void done(Collection objects, Exception e) {

                    }
                });
            }
        });
        bottomSheetDialog.show();
    }

    public void addOne(Blood b) {
        requests.add(0, b);
        adapter.notifyItemInserted(0);
        rcItems.scrollToPosition(0);
    }
}
package com.example.thomas.cookfriends.fragment;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.thomas.cookfriends.R;

import uk.co.senab.photoview.PhotoViewAttacher;


public class ImageDetailFragment extends Fragment {
    public static final int REQUEST_CODE = 0X110;

    private String mImageUrl;
    private ImageView mImageView;

    private PhotoViewAttacher mAttacher;

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment imageDetailFragment = new ImageDetailFragment();

        final Bundle args = new Bundle();
        args.putSerializable("url", imageUrl);
        imageDetailFragment.setArguments(args);

        return imageDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? (String) getArguments().getSerializable("url") : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_image_detail, container, false);
        mImageView = (ImageView) view.findViewById(R.id.image);
        mAttacher = new PhotoViewAttacher(mImageView);

        // 轻点照片，返回消失
        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {
                getActivity().finish();
            }

            @Override
            public void onOutsidePhotoTap() {
                getActivity().finish();
            }
        });


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RequestOptions options = new RequestOptions();
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(ImageDetailFragment.this)
                .load(mImageUrl)
                .apply(options)
                .thumbnail(0.1f)
                .into(mImageView);
        mAttacher.update();

    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQUEST_CODE) {
//            String dialogItemStr = data.getStringExtra(ImageDealDialog.RESPONSE);
//            Log.i(TAG, dialogItemStr);
//
//            switch (dialogItemStr) {
//                case "保存到手机":
//                    Bitmap bitmap = mAttacher.getVisibleRectangleBitmap();
//                    Toast.makeText(getActivity(), "照片已保存（手机相册 -> AbsentM）", Toast.LENGTH_LONG).show();
//                    break;
//            }
//        }
//    }

}



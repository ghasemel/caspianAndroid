package ir.caspiansoftware.caspianandroidapp.PresentationLayer.BasePLL.Gallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.elyasi.android.elyasilib.Dialogs.DatePickerDialog;
import info.elyasi.android.elyasilib.Persian.PersianDate;
import info.elyasi.android.elyasilib.UI.FormActionTypes;
import info.elyasi.android.elyasilib.UI.IActivityCallback;
import info.elyasi.android.elyasilib.UI.IFragmentCallback;
import info.elyasi.android.elyasilib.UI.OnSwipeTouchListener;
import info.elyasi.android.elyasilib.UI.UIUtility;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianFragment;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.KalaPhotoBLL;
import ir.caspiansoftware.caspianandroidapp.Models.KalaModel;
import ir.caspiansoftware.caspianandroidapp.Models.KalaPhotoModel;
import ir.caspiansoftware.caspianandroidapp.Models.PersonModel;
import ir.caspiansoftware.caspianandroidapp.R;
import ir.caspiansoftware.caspianandroidapp.Vars;

/**
 * Created by Ghasem on 4/22/2017.
 */

public class GalleryFragment extends CaspianFragment implements IFragmentCallback {
    private static final String TAG = "GalleryFragment";

    private IActivityCallback mActivityCallback;


    private KalaModel kalaModel;
    private List<KalaPhotoModel> photos;

    private TextView image_title;

    private List<ImageView> imageViews;
    private ImageView currentImage;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gallery;
    }

    @Override
    protected void mapViews(View parentView) {
        Log.d(TAG, "mapViews(): start...");

        if (kalaModel == null) {
            showError(R.string.gallery_null_kala, null);
            return;
        }

        KalaPhotoBLL bll = new KalaPhotoBLL(getContext());
        photos = bll.getKalaPhotosByYearIdAndKalaCode(Vars.YEAR.getId(), kalaModel.getCode());
        if (photos == null || photos.size() == 0) {
            showToast(String.format(getString(R.string.gallery_no_photos_found_for_kala), kalaModel.getName()));
            return;
        }


        image_title = parentView.findViewById(R.id.image_title);

        imageViews = new ArrayList<>();
        imageViews.add(parentView.findViewById(R.id.image1));
        imageViews.add(parentView.findViewById(R.id.image2));
        imageViews.add(parentView.findViewById(R.id.image3));
        imageViews.add(parentView.findViewById(R.id.image4));
        imageViews.add(parentView.findViewById(R.id.image5));
        imageViews.add(parentView.findViewById(R.id.image6));
        imageViews.add(parentView.findViewById(R.id.image7));
        imageViews.add(parentView.findViewById(R.id.image8));
        imageViews.add(parentView.findViewById(R.id.image9));
        imageViews.add(parentView.findViewById(R.id.image10));
        currentImage = parentView.findViewById(R.id.currentImage);

        for (ImageView img : imageViews) {
            img.setOnClickListener(this::subImageClick);
        }

        currentImage.setOnTouchListener(new SwipeListener(getContext()));

        loadPhotos();

        // set first image as current one
        setCurrentImage(0);
    }

    class SwipeListener extends OnSwipeTouchListener {

        public SwipeListener(Context context) {
            super(context);
        }

        @Override
        public void onSwipeRight() {
            Log.d(TAG, "onSwipeRight()");
            swipeRight();
        }

        @Override
        public void onSwipeLeft() {
            Log.d(TAG, "onSwipeLeft()");
            swipeLeft();
        }

        @Override
        public void onSwipeTop() {
            Log.d(TAG, "onSwipeTop()");
        }

        @Override
        public void onSwipeBottom() {
            Log.d(TAG, "onSwipeBottom()");
        }
    }

    private void loadPhotos() {
        if (photos == null || photos.size() == 0)
            return;

        for (int i = 0; i < photos.size(); i++) {
            Bitmap bmp = photos.get(i).getImage();
            imageViews.get(i).setImageBitmap(bmp);
            imageViews.get(i).setTag(i);
        }
    }

    private void setCurrentImage(int index) {
        if (index >= photos.size() || index < 0)
            return;

        KalaPhotoModel photo = photos.get(index);

        image_title.setText(photo.getTitle());

        currentImage.setImageBitmap(photo.getImage());
        currentImage.setTag(index);
    }

    private void swipeLeft() {
        setCurrentImage((Integer) currentImage.getTag() - 1);
    }

    private void swipeRight() {
        setCurrentImage((Integer) currentImage.getTag() + 1);
    }


    private void subImageClick(View img) {
        Log.d(TAG, "subImageClick()");
        setCurrentImage((Integer) img.getTag());
    }

    @Override
    protected void afterMapViews(View parentView) {
        Log.d(TAG, "afterMapViews()");
    }

    @Override
    public ProgressBar getProgressBar() {
        return null;
    }

    @Override
    public void onMyActivityCallback(String actionName, Object parameter, FormActionTypes formActionTypes) {
        Log.d(TAG, "onMyActivityCallback()");
    }

    public KalaModel getKalaModel() {
        return kalaModel;
    }

    public void setKalaModel(KalaModel kala) {
        this.kalaModel = kala;
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick()");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivityCallback = (IActivityCallback) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivityCallback = null;
    }
}

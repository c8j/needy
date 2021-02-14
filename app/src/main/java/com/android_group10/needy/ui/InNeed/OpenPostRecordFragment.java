package com.android_group10.needy.ui.InNeed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.android_group10.needy.Post;
import com.android_group10.needy.R;

import java.util.ArrayList;

public class OpenPostRecordFragment extends Fragment{
        View root;
        ArrayList<Post> dataList;
        Post currentPositioned;
      //  private InNeedViewModel inNeedViewModel;
        private ImageView authorPicture;
        private TextView authorName;
        private TextView authorRating;
        private TextView postDescription;
        private TextView postZipCode;
        private TextView postCity;
        private TextView authorPhone;
        private TextView postIncentive;
        private TextView textPhone;
        private Button acceptPost;
        private Button contactAuthor;

        public OpenPostRecordFragment(Post currentPositioned){
            this.currentPositioned = currentPositioned;
        }

        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {


            root = inflater.inflate(R.layout.open_post, container, false);

            authorPicture = root.findViewById(R.id.author_image);
            authorName = root.findViewById(R.id.author_name);
            authorRating = root.findViewById(R.id.author_rating);
            authorPhone = root.findViewById(R.id.author_phone);
            postDescription = root.findViewById(R.id.post_description);
            postZipCode = root.findViewById(R.id.post_zip);
            postCity = root.findViewById(R.id.post_city);
            postIncentive = root.findViewById(R.id.post_incentive);
            acceptPost = root.findViewById(R.id.accept);
            contactAuthor = root.findViewById(R.id.contact_author);
            textPhone = root.findViewById(R.id.text_phone);

         //   authorPhone.setVisibility(View.INVISIBLE);
         //   textPhone.setVisibility(View.INVISIBLE);


            acceptPost.setOnClickListener(v -> {
                Toast.makeText(getContext(), "change Post status to 2, remove from the list of active", Toast.LENGTH_SHORT).show();
                textPhone.setVisibility(View.VISIBLE);
                authorPhone.setVisibility(View.VISIBLE);
                currentPositioned.setPostStatus(2);
                acceptPost.setClickable(false);
            });
            contactAuthor.setOnClickListener(v ->Toast.makeText(getContext(), "send request to chat to the author", Toast.LENGTH_SHORT).show());

            if (!currentPositioned.getUser().getFirstName().isEmpty()) {
                authorName.setText(currentPositioned.getUser().getFirstName());
            }
            if (currentPositioned.getUser().getImage() != 0) {
                authorPicture.setImageResource(currentPositioned.getUser().getImage());
            }
            if(currentPositioned.getUser().getAuthorRating() !=0) {
                authorRating.setText(String.format("%.1f", currentPositioned.getUser().getAuthorRating()));
            }
            if(!currentPositioned.getDescription().isEmpty()) {
                postDescription.setText(currentPositioned.getDescription());
            }
            if(!currentPositioned.getIncentive().isEmpty()) {
                postIncentive.setText(currentPositioned.getIncentive());
            }
            if(!currentPositioned.getZipCode().isEmpty()) {
                postZipCode.setText(currentPositioned.getZipCode());
            }
            if(!currentPositioned.getCity().isEmpty()) {
                postCity.setText(currentPositioned.getCity());
            }

            return root;
        }
}

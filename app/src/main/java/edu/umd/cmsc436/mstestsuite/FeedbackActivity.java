package edu.umd.cmsc436.mstestsuite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.umd.cmsc436.mstestsuite.R;

/**
 * Created by lisam on 5/1/2017.
 */

public class FeedbackActivity extends AppCompatActivity {

    private Button email_btn;
    private EditText email_content;
    private int requestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        email_btn = (Button) findViewById(R.id.send_email_btn);
        email_content = (EditText) findViewById(R.id.feedback_edit_text);

        email_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                //i.putExtra(Intent.EXTRA_EMAIL, new String[]{"youremail@example.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                i.putExtra(Intent.EXTRA_TEXT, email_content.getText());
                i.setType("message/rfc822");
                startActivityForResult(Intent.createChooser(i, "Choose an Email client :"), requestCode);

                // After email sends, thank and return to main screen. TODO
                if (requestCode == 0) {
                    email_content.setText("Thank you for your feedback. Have a nice day!");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed(); // TODO: doesn't work
    }
}

package firstbelajar.example.tiketsaya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SuccesbuyTicketAct extends AppCompatActivity {

    Button btn_view_ticket, btn_my_dashboard;
    Animation app_splash, btt, ttb;
    ImageView ic_succes_ticket;
    TextView app_title, app_subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_succesbuy_ticket);

        btn_view_ticket = findViewById(R.id.btn_view_ticket);
        btn_my_dashboard = findViewById(R.id.btn_my_dashboard);
        ic_succes_ticket = findViewById(R.id.ic_succes_ticket);
        app_title = findViewById(R.id.app_title);
        app_subtitle = findViewById(R.id.app_subtitle);

        //load animation
        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        btt = AnimationUtils.loadAnimation(this, R.anim.btt);
        ttb = AnimationUtils.loadAnimation(this, R.anim.ttb);

        //run animation
        btn_view_ticket.startAnimation(btt);
        btn_my_dashboard.startAnimation(btt);
        ic_succes_ticket.startAnimation(app_splash);
        app_title.startAnimation(ttb);
        app_subtitle.startAnimation(ttb);

        btn_view_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoprofile = new Intent(SuccesbuyTicketAct.this, MyProfileAct.class);
                startActivity(gotoprofile);
            }
        });

        btn_my_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotohome = new Intent(SuccesbuyTicketAct.this, HomeAct.class);
                startActivity(gotohome);
            }
        });
    }
}

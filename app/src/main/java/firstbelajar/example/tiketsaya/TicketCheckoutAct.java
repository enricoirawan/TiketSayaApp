package firstbelajar.example.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class TicketCheckoutAct extends AppCompatActivity {

    Button btn_buy_ticket, btn_plus, btn_mines;
    TextView text_jumlah_ticket, texttotalHarga, textmyBalance, nama_wisata, lokasi, ketentuan;
    Integer valueJumlahTiket = 1;
    Integer myBalance = 0;
    Integer valuetotalHarga = 0;
    Integer valuehargaTiket = 0;
    ImageView notice_uang;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    String date_wisata = "";
    String time_wisata = "";
    Integer sisaBalance = 0;

    //generate nomor transaksi seara random
    //karena kiat ingin membuat transksi sangat unix
    Integer nomor_transaksi = new Random().nextInt();

    DatabaseReference reference, reference2, reference3, reference4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_checkout);

        getUsernameLocal();

        //mengambil value, dari intent sebelumnya HomeAct
        Bundle bundle = getIntent().getExtras();
        final String jenis_tiket_baru = bundle.getString("jenis_tiket");

        btn_buy_ticket = findViewById(R.id.btn_buy_ticket);
        btn_plus = findViewById(R.id.btn_plus);
        btn_mines = findViewById(R.id.btn_mines);
        text_jumlah_ticket = findViewById(R.id.text_jumlah_ticket);
        notice_uang = findViewById(R.id.notice_uang);
        nama_wisata = findViewById(R.id.nama_wisata);
        lokasi = findViewById(R.id.lokasi);
        ketentuan = findViewById(R.id.ketentuan);
        texttotalHarga = findViewById(R.id.texttotalHarga);
        textmyBalance = findViewById(R.id.textmyBalance);

        //setting value untuk beberapa komponen
        text_jumlah_ticket.setText(valueJumlahTiket.toString());

        //secara default, hide button minus
        btn_mines.animate().alpha(0).setDuration(300).start();
        btn_mines.setEnabled(false);

        //menge set visibilty pada ic_tidak_cukup_uang
        notice_uang.setVisibility(View.GONE);

        //mengambil data user dari firebase
        reference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myBalance = Integer.valueOf(dataSnapshot.child("user_balance").getValue().toString());
                textmyBalance.setText("US$" + myBalance + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //mengambil data dari firebase berdasarkan intent
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //menimpa data yang ada dengan data yang baru
                nama_wisata.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                lokasi.setText(dataSnapshot.child("lokasi").getValue().toString());
                ketentuan.setText(dataSnapshot.child("ketentuan").getValue().toString());

                date_wisata = dataSnapshot.child("date_wisata").getValue().toString();
                time_wisata = dataSnapshot.child("time_wisata").getValue().toString();

                valuehargaTiket = Integer.valueOf(dataSnapshot.child("harga").getValue().toString());

                //setting value total harga
                valuetotalHarga = valuehargaTiket * valueJumlahTiket;
                texttotalHarga.setText("US$" + valuetotalHarga);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_mines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valueJumlahTiket-=1;
                text_jumlah_ticket.setText(valueJumlahTiket.toString());
                if (valueJumlahTiket < 2){
                    btn_mines.animate().alpha(0).setDuration(300).start();
                    btn_mines.setEnabled(false);
                }
                valuetotalHarga = valuehargaTiket * valueJumlahTiket;
                texttotalHarga.setText("US$" + valuetotalHarga);
                if (valuetotalHarga < myBalance) {
                    btn_buy_ticket.animate().translationY(0).alpha(1).setDuration(350).start();
                    btn_buy_ticket.setEnabled(true);
                    textmyBalance.setTextColor(Color.parseColor("#203DD1"));
                    notice_uang.setVisibility(View.GONE);
                }

            }
        });

        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valueJumlahTiket+=1;
                text_jumlah_ticket.setText(valueJumlahTiket.toString());
                if (valueJumlahTiket > 1){
                    btn_mines.animate().alpha(1).setDuration(300).start();
                    btn_mines.setEnabled(true);
                }
                valuetotalHarga = valuehargaTiket * valueJumlahTiket;
                texttotalHarga.setText("US$" + valuetotalHarga);
                if (valuetotalHarga > myBalance) {
                    btn_buy_ticket.animate().translationY(250).alpha(0).setDuration(350).start();
                    btn_buy_ticket.setEnabled(false);
                    textmyBalance.setTextColor(Color.parseColor("#D1206B"));
                    notice_uang.setVisibility(View.VISIBLE);
                }

            }
        });

        btn_buy_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //menyimpan data user kpd firebase, dan mebuat tble baru "my ticket"
                reference3 = FirebaseDatabase.getInstance()
                        .getReference().child("MyTicket")
                        .child(username_key_new).child(nama_wisata.getText().toString() + nomor_transaksi);

                reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        reference3.getRef().child("nama_wisata").setValue(nama_wisata.getText().toString());
                        reference3.getRef().child("id_ticket").setValue(nama_wisata.getText().toString() + nomor_transaksi);
                        reference3.getRef().child("lokasi").setValue(lokasi.getText().toString());
                        reference3.getRef().child("ketentuan").setValue(ketentuan.getText().toString());
                        reference3.getRef().child("date_wisata").setValue(date_wisata);
                        reference3.getRef().child("time_wisata").setValue(time_wisata);
                        reference3.getRef().child("jumlah_tiket").setValue(valueJumlahTiket.toString());

                        Intent gotosuccesticket = new Intent(TicketCheckoutAct.this,SuccesbuyTicketAct.class);
                        startActivity(gotosuccesticket);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                // update data balnace kepada user (yang saat ini login)
                reference4 = FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(username_key_new);
                reference4.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        sisaBalance = myBalance - valuetotalHarga;
                        reference4.getRef().child("user_balance").setValue(sisaBalance);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }

}

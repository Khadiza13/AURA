package com.example.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;


import java.util.ArrayList;

public class women extends AppCompatActivity {

    ArrayList<Constants> arrayList;
    RecyclerView recyclerView;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_women);
        recyclerView=findViewById(R.id.recycler_view);
        arrayList=new ArrayList<>();

        arrayList.add(new Constants("Women and Children Repression Prevention Act, 2000","The penalties under this law vary depending on the severity of the offense. For example, it includes imprisonment for various terms, ranging from a few years to life imprisonment or the death penalty in cases of extreme offenses such as rape",false));
        arrayList.add(new Constants("Dowry Prohibition Act, 1980","The Dowry Prohibition Act stipulates penalties for both giving and taking dowry. Violations can result in imprisonment and fines",false));
        arrayList.add(new Constants("Prevention of Domestic Violence Act, 2010","This law allows for the issuance of protection orders to ensure the safety of victims. Violation of protection orders may lead to imprisonment and fines for the offender",false));
        arrayList.add(new Constants("Acid Offences Prevention Act, 2002","The Acid Offences Prevention Act imposes strict penalties for acid attacks, including imprisonment and fines. The severity of the punishment depends on the extent of injury caused to the victim",false));
        arrayList.add(new Constants("Women and Children Repression Prevention (Amendment) Act, 2003","This amendment enhanced the penalties for offenses covered under the original Act. For example, it increased the punishment for certain forms of sexual assault, including rape",false));
        arrayList.add(new Constants("Punishment for rape Act, 2000","If any man rapes any woman or child, he shall be punishable with death penalty or rigorous imprisonment for life and shall also be liable to fine in addition thereto",false));
        arrayList.add(new Constants("Protective Custody Act, 2000","If, during the trial of any offense under this Act, the Tribunal considers that it is necessary to keep any woman or child in protective custody, the Tribunal shall remand the said woman or child to the custody of the public authority outside the prison and to the custody of such other person or agency as the Tribunal may think fit for the purpose",false));
        arrayList.add(new Constants("Punishment for causing death for dowry, 2000","He will be punishable with death for causing death or with imprisonment for life for attempt to cause death and in both cases shall be liable to fine in addition to the said sentence",false));

        adapter=new Adapter(arrayList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
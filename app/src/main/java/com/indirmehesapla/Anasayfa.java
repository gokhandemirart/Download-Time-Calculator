package com.indirmehesapla;

import android.net.wifi.WifiInfo;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.indirmehesapla.Helper.EqualSpacingItemDecoration;
import com.indirmehesapla.Model.Tur;
import com.indirmehesapla.Adapter.TurListAdapter;
import com.indirmehesapla.Model.TurAdapterList;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Anasayfa extends AppCompatActivity {


    //GenelAyarlar
    private ArrayList<Tur> TurArray= new ArrayList<>();
    private ArrayList<String> HizTurAdi =  new ArrayList<String>();
    private ArrayList<Integer> HizTurKbps =  new ArrayList<Integer>();
    private Integer dosyaTuru = 1000000 ,DosyaBoyutu =1 , baglantiHiziTur = 1000 , baglantiHizi = 0;
    private Spinner mySpinner;
    private TextView BaglantiHiziSonuc;

    //Adapter Ayar
    private RecyclerView recyclerView;
    private TurListAdapter adapter;
    private List<TurAdapterList> turList;
    private GridLayoutManager gridLayoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mySpinner = (Spinner) findViewById(R.id.DropDownHizList);
        BaglantiHiziSonuc = findViewById(R.id.BaglantiHiziSonuc);

        ArrayAdapter<String> hizTurAdapter = new ArrayAdapter<String>(Anasayfa.this,
            R.layout.spinner_item_hiz, getResources().getStringArray(R.array.HizTurDropDown));

        hizTurAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mySpinner.setAdapter(hizTurAdapter);
        mySpinner.setSelection(1);

        HizTurAdi.add("Kbps");
        HizTurAdi.add("Mbps");
        HizTurAdi.add("Gbps");
        HizTurKbps.add(1);
        HizTurKbps.add((int)1e3);
        HizTurKbps.add((int)1e6);
        DosyaBoyutu=1;


        TurArray.add(new Tur(1,"2G GPRS", (double)171, 0));
        TurArray.add(new Tur(1,"2G EDGE",(double)384,1));
        TurArray.add(new Tur(1,"3G UMTS",2e3,1));
        TurArray.add(new Tur(1,"3G HSPA",7e3,1));
        TurArray.add(new Tur(1,"3G HSPA+",21e3,1));
        TurArray.add(new Tur(1,"3G DC-HSPA+",42e3,1));
        TurArray.add(new Tur(1,"4G LTE",1e5,1));
        TurArray.add(new Tur(1,"4.5G LTE ADV.",3e5,1));
        TurArray.add(new Tur(1,"ADSL",8e3,1));
        TurArray.add(new Tur(1,"ADSL2",16e3,1));
        TurArray.add(new Tur(1,"ADSL2+",24e3,1));
        TurArray.add(new Tur(1,"VDSL",5e4,1));
        TurArray.add(new Tur(1,"VDSL2",1e5,1));
        TurArray.add(new Tur(1,"FIBER",1e6,2 ));


        turList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.TurListele);

        recyclerView.addItemDecoration(new EqualSpacingItemDecoration(1, 8, true, 0));

        gridLayoutManager = new GridLayoutManager(this, 1);

        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new TurListAdapter(this, turList);

        recyclerView.setAdapter(adapter);



        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.dosya_boyutu);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioGroup.getCheckedRadioButtonId();

                RadioButton radioButton = (RadioButton) findViewById(selectedId);

                if(radioButton.getText().toString().equals("KB")){
                    dosyaTuru = 1;
                }else if(radioButton.getText().toString().equals("MB")){
                    dosyaTuru = 1000;
                }else if(radioButton.getText().toString().equals("GB")){
                    dosyaTuru = 1000000;
                }
                TurGetir(DosyaBoyutu,dosyaTuru);
            }
        });

        TurGetir(DosyaBoyutu,dosyaTuru);

        final EditText DosyaBoyutuInput = (EditText) findViewById(R.id.DosyaBoyutuInput);
        DosyaBoyutuInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                TurGetir(DosyaBoyutu,dosyaTuru);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                DosyaBoyutuInput.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                            return true;
                        }
                        return false;
                    }
                });
                if(s.toString().equals("")) {
                    DosyaBoyutu = DosyaBoyutu;
                }else{
                    DosyaBoyutu = Integer.parseInt(s.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                TurGetir(DosyaBoyutu,dosyaTuru);
            }
        });


        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                Object item = parentView.getItemAtPosition(position);


                if(item.toString().equals("Kbps")){
                    baglantiHiziTur = 1;
                }else if(item.toString().equals("Mbps")){
                    baglantiHiziTur = 1000;
                }else if(item.toString().equals("Gbps")){
                    Log.d("xzczcx","sdasdsa"+item.toString());
                    baglantiHiziTur = 1000000;
                }
                BaglantiHiziHesapla(baglantiHiziTur,DosyaBoyutu,baglantiHizi);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        final EditText BaglantiHiziInput = (EditText) findViewById(R.id.BaglantiHizi);
        BaglantiHiziInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                BaglantiHiziHesapla(baglantiHiziTur,DosyaBoyutu,baglantiHizi);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                BaglantiHiziInput.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                            return true;
                        }
                        return false;
                    }
                });
                if(s.toString().equals("")) {
                    baglantiHizi = baglantiHizi;
                }else{
                    baglantiHizi = Integer.parseInt(s.toString());
                }

                BaglantiHiziHesapla(baglantiHiziTur,DosyaBoyutu,baglantiHizi);
            }

            @Override
            public void afterTextChanged(Editable s) {
                BaglantiHiziHesapla(baglantiHiziTur,DosyaBoyutu,baglantiHizi);
            }
        });



    }

    public void TurGetir(Integer DosyaBoyutu,Integer DosyaTuru){

        turList.clear();
        for (int i = 0; i < TurArray.size(); i++) {

            Integer id = TurArray.get(i).ID;
            String Adi = TurArray.get(i).Adi;
            Double kbps = TurArray.get(i).kbps / HizTurKbps.get(TurArray.get(i).units);

            HashMap<String, String> contact = new HashMap<>();

            NumberFormat nf = new DecimalFormat("##.###");
            String HizTuru =  nf.format(kbps) +" " + HizTurAdi.get(TurArray.get(i).units);
            Integer SureHesapla = (int)((double) TurArray.get(i).kbps);
            String SureSonuc = SureHesapla((DosyaBoyutu*DosyaTuru)*8 ,SureHesapla);


            TurAdapterList a = new TurAdapterList( id.toString(),Adi, HizTuru,SureSonuc);
            turList.add(a);
        }

        adapter.notifyDataSetChanged();
        BaglantiHiziHesapla(baglantiHiziTur,DosyaBoyutu,baglantiHizi);
    }

    public void BaglantiHiziHesapla(Integer baglantiHiziTur,Integer DosyaBoyutu, Integer baglantiHizi){

          String SureSonuc = SureHesapla((DosyaBoyutu*dosyaTuru)*8 ,baglantiHizi*baglantiHiziTur);


          BaglantiHiziSonuc.setText(SureSonuc);


    }

    public String SureHesapla(Integer DosyaTuru, Integer Kbps ){

            String t = "0 Sec.";
            if (0 != DosyaTuru && 0 != Kbps) {
                Integer a = DosyaTuru/Kbps,
                        s = a / 86400,
                        i = (a / 3600) % 24,
                        u = (a / 60) % 60,
                        d = a % 60;
                t = "";
                if( s > 0 ){
                    t += s + " Day ";
                }
                if(s > 0 || i > 0) {
                    t += i + " Hour ";
                }
                if(s > 0 || i > 0 || u > 0) {
                    t += u + " Minute ";
                }
                t += d + " Sec. ";

            }
            return t;
    }
}

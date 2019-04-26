package com.androiddeft.loginandregistration;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

public class DashboardActivity extends AppCompatActivity {
    private SessionHandler session;
    private Button generate,scan;
    private EditText editText;
    private String kilos;
    private Spinner spinner;
    private ImageView qr_code;
    private String recyclables;
    //User user = session.getUserDetails();
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";

    private static final String KEY_USERID = "user_id";
    private static final String KEY_POINTS = "points";
    private static final String KEY_EMPTY = "";

    private String points;

    private ProgressDialog pDialog;
    //private String updateUser_url = "http://192.168.1.14/member/updateUser.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        TextView textViewPoints = findViewById(R.id.points);
        //textViewPoints.setText("You have " + user.getPoints());
        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();
        final String username = user.getUsername();

        generate = findViewById(R.id.generate);
        scan = findViewById(R.id.scan);
        editText = findViewById(R.id.text);

        qr_code = findViewById(R.id.qrcode);
        spinner = findViewById(R.id.spinner);
        recyclables = spinner.getSelectedItem().toString();

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kilos = editText.getText().toString();
                //String strUserId = String.valueOf(userId);
                if((recyclables!=null && !recyclables.isEmpty())&&(kilos!=null && !kilos.isEmpty())){
                    try{
                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                        BitMatrix bitMatrix = multiFormatWriter.encode(recyclables + "\n" + kilos + "\n" + username, BarcodeFormat.QR_CODE,500,500);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        qr_code.setImageBitmap(bitmap);
                    } catch (WriterException e){
                        e.printStackTrace();
                    }
                }
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(DashboardActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setCameraId(0);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setPrompt("Scanning");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setBarcodeImageEnabled(true);
                intentIntegrator.initiateScan();
            }
        });

        Button logoutBtn = findViewById(R.id.btnLogout);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
                Intent i = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(i);
                finish();

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result!=null && result.getContents()!=null){

            String strResult = result.getContents();
            String[] arrResult = strResult.split("\n");
            //final int intUserId = Integer.parseInt(userId);
            String recyclables = arrResult[0];
            double kilos = Double.valueOf(arrResult[1]),price = 0;
            double amount = 0;
            String junkshopOwner = arrResult[2];

            if(recyclables.equalsIgnoreCase("White Paper")){
                price = 8.00;
            } else if (recyclables.equalsIgnoreCase("Cartons")){
                price = 2.50;
            } else if (recyclables.equalsIgnoreCase("Newspaper")){
                price = 4.00;
            } else if (recyclables.equalsIgnoreCase("Assorted Papers")){
                price = 1.50;
            } else if (recyclables.equalsIgnoreCase("Clean PET Bottle")){
                price = 16.00;
            } else if (recyclables.equalsIgnoreCase("Unclean PET Bottle")){
                price = 12.00;
            } else if (recyclables.equalsIgnoreCase("Aluminum Cans")){
                price = 50.00;
            } else if (recyclables.equalsIgnoreCase("Plastic (HDPE)")){
                price = 10.00;
            } else if (recyclables.equalsIgnoreCase("Plastic (LDPE)")){
                price = 5.00;
            } else if (recyclables.equalsIgnoreCase("Engineering Plastics")){
                price = 10.00;
            } else if (recyclables.equalsIgnoreCase("Copper Wire (A)")){
                price = 300.00;
            } else if (recyclables.equalsIgnoreCase("Copper Wire (B)")){
                price = 250.00;
            } else if (recyclables.equalsIgnoreCase("Copper Wire (C)")){
                price = 150.00;
            } else if (recyclables.equalsIgnoreCase("Iron Alloys")){
                price = 9.00;
            } else if (recyclables.equalsIgnoreCase("Stainless Steel")){
                price = 60.00;
            } else if (recyclables.equalsIgnoreCase("GI Sheet")){
                price = 7.00;
            } else if (recyclables.equalsIgnoreCase("Tin Can")){
                price = 3.00;
            } else if (recyclables.equalsIgnoreCase("Emperador Long Neck")){
                price = 1.50;
            } else if (recyclables.equalsIgnoreCase("Emperador Lapad")){
                price = 0.75;
            } else if (recyclables.equalsIgnoreCase("Ginebra Gin")){
                price = 0.65;
            } else if (recyclables.equalsIgnoreCase("Ketchup")){
                price = 0.25;
            } else if (recyclables.equalsIgnoreCase("Softdrinks Bottle")){
                price = 2.00;
            } else if (recyclables.equalsIgnoreCase("Glass Cullets")){
                price = 1.00;
            } else if (recyclables.equalsIgnoreCase("Old Diskette")){
                price = 8.00;
            } else if (recyclables.equalsIgnoreCase("Inkjet Cartridge")){
                price = 200;
            } else if (recyclables.equalsIgnoreCase("Car Battery")){
                price = 100.00;
            } else if (recyclables.equalsIgnoreCase("Others")){
                price = 0.20;
            }

            amount = price * kilos;


            String strData =
                    "Junkshop Owner: " + junkshopOwner +
                            "\nJunk Type: " + recyclables +
                            "\nPrice per kilo: " + price +
                                "\nWeight: " + kilos +
                                    "\nTotal Amount: " + amount;

            final double finalAmount = amount;
            new AlertDialog.Builder(DashboardActivity.this)
                    .setTitle("Transfer Points")
                    .setMessage(strData)
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //addPoints(intUserId, String.valueOf(finalAmount));
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void displaySaveLoader() {
        pDialog = new ProgressDialog(DashboardActivity.this);
        pDialog.setMessage("Saving.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    /*private void addPoints(int userId, String points) {
        displaySaveLoader();
        JSONObject request = new JSONObject();
        String strUserId = String.valueOf(userId);
        try {
            //Populate the request parameters
            //request.put(KEY_USERID, strUserId);
            //request.put(KEY_POINTS, points);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/
}

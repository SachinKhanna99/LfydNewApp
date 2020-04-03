package com.letsdevelopit.lfydnewapp.ui.qrscanner;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.letsdevelopit.lfydnewapp.R;


public class QR_Scanner extends Fragment {
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private final String LOGTAG = "QRCScanner-MainActivity";
    private QRScannerViewModel qrViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


                Intent i = new Intent(getContext(), QrCodeActivity.class);
                startActivityForResult( i,REQUEST_CODE_QR_SCAN);
        // setDrawerLocked(true);

        return null;
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode != Activity.RESULT_OK)
        {
//            // Log.d(LOGTAG,"COULD NOT GET A GOOD RESULT.");
            if(data==null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if( result!=null)
            {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Scan Error");
                alertDialog.setMessage("QR Code could not be scanned");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            return;

        }
        if(requestCode == REQUEST_CODE_QR_SCAN)
        {
            if(data==null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");

            try {
                final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));

//                // Log.d(LOGTAG,"Have scan result in your app activity :"+ result);
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Scan result");
                alertDialog.setMessage(result);

                if (result.contains("upi://pay")){

                    Intent intent = new Intent(getContext(), PaymentActivity.class);
                    intent.putExtra("upi_id",result.substring(13,result.indexOf('&')));
                    intent.putExtra("upi_result",result);
                    startActivity(intent);


                }else {
                    Toast.makeText(getContext(), "Make Sure You Are Scanning Upi Qr code", Toast.LENGTH_SHORT).show();

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(browserIntent);
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }catch (Exception e){
                Toast.makeText(getContext(), "Make Sure You Are Scanning Upi Qr code", Toast.LENGTH_SHORT).show();
                final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));

                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Scan result, Want to open in Browser");
                alertDialog.setMessage(result);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(browserIntent);
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }

        }
    }

}

package com.example.qrscaner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrscaner.Model.QrEmail;
import com.example.qrscaner.Model.QrMess;
import com.example.qrscaner.Model.QrProduct;
import com.example.qrscaner.Model.QrScan;
import com.example.qrscaner.Model.QrText;
import com.example.qrscaner.Model.QrUrl;
import com.example.qrscaner.Model.QrWifi;
import com.example.qrscaner.Model.QreTelephone;
import com.example.qrscaner.R;
import com.example.qrscaner.view.fonts.TextViewPoppinBold;
import com.example.qrscaner.view.fonts.TextViewPoppinThin;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolderHistory> {
    private List<QrScan> qrList;
    private boolean isEdit;
    private Context mContext;
    private IShareData shareDataListener;
    private IDeleteQr deleteQrListener;
    private List<QrScan> qrScanListEdit;


    public HistoryAdapter(List<QrScan> qrList, ISelectItem  selectItem,boolean isEdit, Context mContext, IShareData shareDataListener, IDeleteQr deleteQrListener, IDeleteQr deleteListener, CallEditListener callEdit) {
        this.qrList = qrList;
        this.isEdit = isEdit;
        this.mContext = mContext;
        this.shareDataListener = shareDataListener;
        this.deleteQrListener = deleteQrListener;
        this.deleteListener = deleteListener;
        this.callEdit = callEdit;
        this.selectItem = selectItem;
    }

    @NonNull
    @Override
    public ViewHolderHistory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_scan, parent, false);
        return new ViewHolderHistory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderHistory holder, int position) {
        QrScan qrScan = qrList.get(position);
        holder.tvItemHistoryDate.setText(qrScan.getDateString());
        if (position > 0) {
            QrScan qrScanUndo = qrList.get(position - 1);
            if (qrScanUndo.getDateString().equals(qrScan.getDateString())) {
                holder.ctlItemHistoryDate.setVisibility(View.GONE);
            }
        } else {
            holder.ctlItemHistoryDate.setVisibility(View.VISIBLE);

        }
        String[] content = qrScan.getScanText().split(":");
        if (content[0].equals("SMSTO")) {
            QrMess qrMess = new QrMess();
            qrMess.compileSMS(content);
            holder.tvItemHistoryQrContent.setText(qrMess.getSendBy());
            holder.imvItemScanType.setImageResource(R.drawable.add_sms);
            holder.tvItemHistoryQrDate.setText(qrScan.getDateString());
            if (qrScan.getScanText().equals("")) {
                holder.tvItemHistoryQrContent.setText("SMS");
            }


        } else if (content[0].equals("Error")) {
            holder.tvItemHistoryQrContent.setText("Error");
            holder.imvItemScanType.setImageResource(R.drawable.ic_error);
            holder.tvItemHistoryQrDate.setText(qrScan.getDateString());
            if (qrScan.getScanText().equals("")) {
                holder.tvItemHistoryQrContent.setText("ERROR");
            }

        } else if (content[0].equals("http") || content[0].equals("https")) {
            QrUrl qrUrl = new QrUrl();
            qrUrl.compileUrl(content);
            holder.tvItemHistoryQrContent.setText(qrUrl.getUrl());
            holder.imvItemScanType.setImageResource(R.drawable.add_uri);
            holder.tvItemHistoryQrDate.setText(qrScan.getDateString());
            if (qrScan.getScanText().equals("")) {
                holder.tvItemHistoryQrContent.setText("Uri");
            }
        } else if (content[0].equals("WIFI")) {
            StringBuilder stringBuilder = new StringBuilder();
            String[] contentWifi = qrScan.getScanText().split(";");
            for (String value : contentWifi) {
                stringBuilder.append(value);
            }
            String contentWifi2 = stringBuilder.toString();
            String[] contentWifi3 = contentWifi2.split(":");
            QrWifi qrWifi = new QrWifi();
            qrWifi.compileWifi(contentWifi, contentWifi3);
            holder.tvItemHistoryQrContent.setText(qrWifi.getWifiName());
            holder.imvItemScanType.setImageResource(R.drawable.add_wifi);
            holder.tvItemHistoryQrDate.setText(qrScan.getDateString());

            if (qrScan.getScanText().equals("")) {
                holder.tvItemHistoryQrContent.setText("Wifi");
            }
        } else if (content[0].equals("MATMSG")) {
            QrEmail qrEmail = new QrEmail();
            StringBuilder stringBuilder = new StringBuilder();
            for (String value : content) {
                stringBuilder.append(value);
            }
            qrEmail.compileEmail(content);
            holder.tvItemHistoryQrContent.setText(qrEmail.getSendBy());
            holder.imvItemScanType.setImageResource(R.drawable.add_email);
            holder.tvItemHistoryQrDate.setText(qrScan.getDateString());
            if (qrScan.getScanText().equals("")) {
                holder.tvItemHistoryQrContent.setText("Email");
            }
        } else if (content[0].equals("tel")) {
            QreTelephone qreTelephone = new QreTelephone();
            qreTelephone.compile(content);
            holder.tvItemHistoryQrContent.setText(qreTelephone.getTel());
            holder.imvItemScanType.setImageResource(R.drawable.add_call);
            holder.tvItemHistoryQrDate.setText(qrScan.getDateString());
            if (qrScan.getScanText().equals("")) {
                holder.tvItemHistoryQrContent.setText("Phone");
            }

        } else {

            if (checkIsProduct(qrScan.getScanText())) {
                holder.tvItemHistoryQrContent.setText(qrScan.getScanText());
                holder.imvItemScanType.setImageResource(R.drawable.ic_product);
                holder.tvItemHistoryQrDate.setText(qrScan.getDateString());
                if (qrScan.getScanText().equals("")) {
                    holder.tvItemHistoryQrContent.setText("Product");
                }

            } else {
                holder.tvItemHistoryQrContent.setText(qrScan.getScanText());
                holder.imvItemScanType.setImageResource(R.drawable.add_text);
                holder.tvItemHistoryQrDate.setText(qrScan.getDateString());
                if (qrScan.getScanText().equals("")) {
                    holder.tvItemHistoryQrContent.setText("Text");
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (qrList != null) {
            return qrList.size();
        }
        return 0;
    }

    public boolean checkIsProduct(String qr) {
        long id;
        try {
            id = Long.parseLong(qr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public class ViewHolderHistory extends RecyclerView.ViewHolder {
        ConstraintLayout ctlItemHistoryDate, ctlHistoryItem;
        TextViewPoppinBold tvItemHistoryQrContent;
        TextView tvItemHistoryDate;
        TextViewPoppinThin tvItemHistoryQrDate;
        CardView cvItemHistoryQr, cvItemHistoryEdit;
        ImageView imvItemScanType, imvItemScanMenu, imvItemCheck;
        LinearLayout imvItemCancel, imvItemShare, imvItemEdit, imvItemDelete;

        public ViewHolderHistory(@NonNull View itemView) {
            super(itemView);
            ctlHistoryItem = itemView.findViewById(R.id.ctl_history_item);
            tvItemHistoryDate = itemView.findViewById(R.id.tv_item_history_monthCreate);
            ctlItemHistoryDate = itemView.findViewById(R.id.csl_item_history_date);
            cvItemHistoryQr = itemView.findViewById(R.id.cv_item_history_scan);
            imvItemCheck = itemView.findViewById(R.id.imv_item_history_scan_check);
            tvItemHistoryQrContent = itemView.findViewById(R.id.tv_item_scan_content);
            tvItemHistoryQrDate = itemView.findViewById(R.id.tv_item_scan_date_create);
            imvItemScanMenu = itemView.findViewById(R.id.imv_item_scan_menu);
            imvItemScanType = itemView.findViewById(R.id.imv_item_scan_type);
            cvItemHistoryEdit = itemView.findViewById(R.id.cv_item_history_edit);
            imvItemCancel = itemView.findViewById(R.id.lnl_item_history_cancel);
            imvItemDelete = itemView.findViewById(R.id.lnl_item_history_delete);
            imvItemEdit = itemView.findViewById(R.id.lnl_item_history_edit);
            imvItemShare = itemView.findViewById(R.id.lnl_item_history_share);
            cvItemHistoryEdit.setVisibility(View.GONE);
            if (!isEdit) {
                imvItemCheck.setVisibility(View.GONE);
                qrScanListEdit = new ArrayList<>();
                selectItem.selectItemListener(qrScanListEdit);
            }
            imvItemEdit.setOnClickListener(view -> {
                callEdit.edit(isEdit);
                imvItemCheck.setVisibility(View.GONE);
            });
            imvItemCancel.setOnClickListener(view -> {
                cvItemHistoryQr.setVisibility(View.VISIBLE);
                cvItemHistoryEdit.setVisibility(View.GONE);
            });
            imvItemDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    QrScan qrScan = qrList.get(getLayoutPosition());
                    deleteListener.deleteListener(qrScan);
                }
            });

            imvItemScanMenu.setOnClickListener(view -> {
                cvItemHistoryQr.setVisibility(View.GONE);
                cvItemHistoryEdit.setVisibility(View.VISIBLE);

            });
            imvItemShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    QrScan qrScan = qrList.get(getLayoutPosition());
                    String[] content = qrScan.getScanText().split(":");
                    if (qrScan.getTypeQR() == QrScan.QRType.EMAIL) {
                        QrEmail qrEmail = new QrEmail();
                        qrEmail.compileEmail(content);
                        shareDataListener.shareDataListener(qrEmail.getShare());
                    } else if (qrScan.getTypeQR() == QrScan.QRType.SMS) {
                        QrMess qrMess = new QrMess();
                        qrMess.compileSMS(content);
                        shareDataListener.shareDataListener(qrMess.getShare());
                    } else if (qrScan.getTypeQR() == QrScan.QRType.PRODUCT) {
                        QrProduct qrProduct = (QrProduct) qrScan;
                        qrProduct.compileProduct(qrScan.getScanText());
                        shareDataListener.shareDataListener(qrProduct.getShare());
                    } else if (qrScan.getTypeQR() == QrScan.QRType.WIFI) {
                        QrWifi qrWifi = new QrWifi();
                        StringBuilder stringBuilder = new StringBuilder();
                        String[] contentWifi = qrScan.getScanText().split(";");
                        for (String value : contentWifi) {
                            stringBuilder.append(value);
                        }
                        String contentWifi2 = stringBuilder.toString();
                        String[] contentWifi3 = contentWifi2.split(":");
                        qrWifi.compileWifi(contentWifi, contentWifi3);
                        shareDataListener.shareDataListener(qrWifi.getShare());
                    } else if (qrScan.getTypeQR() == QrScan.QRType.PHONE) {
                        QreTelephone qreTelephone = new QreTelephone();
                        qreTelephone.compile(content);
                        shareDataListener.shareDataListener(qreTelephone.getShare());
                    } else if (qrScan.getTypeQR() == QrScan.QRType.TEXT) {
                        QrText qrText = new QrText();
                        shareDataListener.shareDataListener(qrText.getShare());
                    } else if (qrScan.getTypeQR() == QrScan.QRType.URL) {
                        QrUrl qrUrl = new QrUrl();
                        shareDataListener.shareDataListener(qrUrl.getShare());
                    }
                }
            });
            if (isEdit) {
                if (qrScanListEdit == null) {
                    qrScanListEdit = new ArrayList<>();
                }
                itemView.setOnClickListener(view -> {
                    QrScan qrScan = qrList.get(getLayoutPosition());
                 try {
                     if (!qrScan.getIsEdit()) {
                         imvItemCheck.setImageResource(R.drawable.ic_check);
                         ctlHistoryItem.setBackgroundResource(R.drawable.background_boder_selected);
                         qrScan.setEdit(true);
                         qrScanListEdit.add(getLayoutPosition(), qrList.get(getLayoutPosition()));

                     } else {
                         imvItemCheck.setImageResource(R.drawable.ic_uncheck);
                         ctlHistoryItem.setBackgroundResource(R.drawable.background_boder_unselect);
                         qrScan.setEdit(false);
                         if (qrScanListEdit.size()>0){
                             qrScanListEdit.remove(getLayoutPosition());
                         }
                     }
                     selectItem.selectItemListener(qrScanListEdit);
                 }catch (Exception e){
                     qrScanListEdit = new ArrayList<>();
                     selectItem.selectItemListener(qrScanListEdit);
                 }
                });
            }

        }
    }

    private IDeleteQr deleteListener;

    public interface IShareData {
        void shareDataListener(String data);
    }

    private final CallEditListener callEdit;

    public interface CallEditListener {
        void edit(Boolean isEdit);
    }

    public interface IDeleteQr {
        void deleteListener(QrScan qrScan);
    }

    private ISelectItem selectItem;

    public interface ISelectItem {
        void selectItemListener(List<QrScan> qrScanList);
    }
}

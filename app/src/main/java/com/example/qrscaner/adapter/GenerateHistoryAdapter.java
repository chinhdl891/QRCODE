package com.example.qrscaner.adapter;

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

import com.example.qrscaner.Model.GenerateItem;
import com.example.qrscaner.Model.QrEmail;
import com.example.qrscaner.Model.QrGenerate;
import com.example.qrscaner.Model.QrMess;
import com.example.qrscaner.Model.QrProduct;
import com.example.qrscaner.Model.QrGenerate;
import com.example.qrscaner.Model.QrScan;
import com.example.qrscaner.Model.QrText;
import com.example.qrscaner.Model.QrUrl;
import com.example.qrscaner.Model.QrWifi;
import com.example.qrscaner.Model.QreTelephone;
import com.example.qrscaner.R;
import com.example.qrscaner.view.fonts.TextViewPoppinBold;
import com.example.qrscaner.view.fonts.TextViewPoppinThin;

import java.util.List;

public class GenerateHistoryAdapter extends RecyclerView.Adapter<GenerateHistoryAdapter.GenerateQrCodeHistoryViewHolder> {
    private List<QrGenerate> generateItemList;
    private boolean isEdit;

    public GenerateHistoryAdapter(List<QrGenerate> generateItemList, boolean isEdit, IEdit editListener, IShare shareListener, IDelete deleteListener) {
        this.generateItemList = generateItemList;
        this.isEdit = isEdit;
        this.editListener = editListener;
        this.shareListener = shareListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public GenerateQrCodeHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_scan, parent, false);
        return new GenerateQrCodeHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenerateQrCodeHistoryViewHolder holder, int position) {
        if (!isEdit){
            holder.imvItemCheck.setVisibility(View.GONE);
        }else {
            holder.imvItemCheck.setVisibility(View.VISIBLE);
        }
        holder.cvItemHistoryEdit.setVisibility(View.GONE);
        if (isEdit) {
            holder.imvItemEdit.setVisibility(View.GONE);
        }
        QrGenerate qrGenerate = generateItemList.get(position);
        holder.tvItemHistoryDate.setText(qrGenerate.getStringDate());
        if (position > 0) {
            QrGenerate qrScanUndo = generateItemList.get(position - 1);
            if (qrScanUndo.getStringDate().equals(qrGenerate.getStringDate())) {
                holder.ctlItemHistoryDate.setVisibility(View.GONE);
            }
        } else {
            holder.ctlItemHistoryDate.setVisibility(View.VISIBLE);
        }
        String[] content = qrGenerate.getContent().split(":");
        if (content[0].equals("SMSTO")) {
            QrMess qrMess = new QrMess();
            qrMess.compileSMS(content);
            holder.tvItemHistoryQrContent.setText(qrMess.getSendBy());
            holder.imvItemScanType.setImageResource(R.drawable.add_sms);
            holder.tvItemHistoryQrDate.setText(qrGenerate.getStringDate());
            if (qrGenerate.getContent().equals("")) {
                holder.tvItemHistoryQrContent.setText("SMS");
            }


        } else if (content[0].equals("http") || content[0].equals("https")) {
            QrUrl qrUrl = new QrUrl();
            qrUrl.compileUrl(content);
            holder.tvItemHistoryQrContent.setText(qrUrl.getUrl());
            holder.imvItemScanType.setImageResource(R.drawable.add_uri);
            holder.tvItemHistoryQrDate.setText(qrGenerate.getStringDate());
//            holder.imvItemShare.setOnClickListener(view -> shareDataListener.shareDataListener(qrUrl.getUrl()));
            if (qrGenerate.getContent().equals("")) {
                holder.tvItemHistoryQrContent.setText("Uri");
            }
        } else if (content[0].equals("WIFI")) {
            StringBuilder stringBuilder = new StringBuilder();
            String[] contentWifi = qrGenerate.getContent().split(";");
            for (String value : contentWifi) {
                stringBuilder.append(value);
            }
            String contentWifi2 = stringBuilder.toString();
            String[] contentWifi3 = contentWifi2.split(":");
            QrWifi qrWifi = new QrWifi();
            qrWifi.compileWifi(contentWifi, contentWifi3);
            holder.tvItemHistoryQrContent.setText(qrWifi.getWifiName());
            holder.imvItemScanType.setImageResource(R.drawable.add_wifi);
            holder.tvItemHistoryQrDate.setText(qrGenerate.getStringDate());
//            holder.imvItemShare.setOnClickListener(view -> shareDataListener.shareDataListener(qrWifi.getShare()));
            if (qrGenerate.getContent().equals("")) {
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
            holder.tvItemHistoryQrDate.setText(qrEmail.getDateString());
            if (qrGenerate.getContent().equals("")) {
                holder.tvItemHistoryQrContent.setText("Email");
            }
//            holder.imvItemShare.setOnClickListener(view -> shareDataListener.shareDataListener(qrEmail.getShare()));
        } else if (content[0].equals("tel")) {
            QreTelephone qreTelephone = new QreTelephone();
            qreTelephone.compile(content);
            holder.tvItemHistoryQrContent.setText(qreTelephone.getTel());
            holder.imvItemScanType.setImageResource(R.drawable.add_call);
            holder.tvItemHistoryQrDate.setText(qreTelephone.getDateString());
            if (qrGenerate.getContent().equals("")) {
                holder.tvItemHistoryQrContent.setText("Phone");
            }
//            holder.imvItemShare.setOnClickListener(view -> shareDataListener.shareDataListener(qreTelephone.getTel()));
        } else {
            QrText qrText = new QrText();
            qrText.setText(qrGenerate.getContent());
            holder.tvItemHistoryQrContent.setText(qrGenerate.getContent());
            holder.imvItemScanType.setImageResource(R.drawable.add_text);
            holder.tvItemHistoryQrDate.setText(qrGenerate.getStringDate());

//                holder.imvItemShare.setOnClickListener(view -> shareDataListener.shareDataListener(qrText.getText()));
        }
    }


    @Override
    public int getItemCount() {
        if (generateItemList != null) {
            return generateItemList.size();
        }
        return 0;
    }

    public class GenerateQrCodeHistoryViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout ctlItemHistoryDate;
        TextViewPoppinBold tvItemHistoryQrContent;
        TextView tvItemHistoryDate;
        TextViewPoppinThin tvItemHistoryQrDate;
        CardView cvItemHistoryQr, cvItemHistoryEdit;
        ImageView imvItemScanType, imvItemScanMenu, imvItemCheck;
        LinearLayout imvItemCancel, imvItemShare, imvItemEdit, imvItemDelete;

        public GenerateQrCodeHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
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

            imvItemCheck.setVisibility(View.GONE);
            imvItemScanMenu.setOnClickListener(view -> {
                cvItemHistoryQr.setVisibility(View.GONE);
                cvItemHistoryEdit.setVisibility(View.VISIBLE);

            });
            imvItemCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cvItemHistoryQr.setVisibility(View.VISIBLE);
                    cvItemHistoryEdit.setVisibility(View.GONE);
                }
            });
            imvItemShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    {
                        QrGenerate qrGenerate = generateItemList.get(getLayoutPosition());
                        String[] content = qrGenerate.getContent().split(":");
                        if (qrGenerate.getQrType() == QrScan.QRType.EMAIL) {
                            QrEmail qrEmail = new QrEmail();
                            qrEmail.compileEmail(content);
                            shareListener.share(qrEmail.getShare());
                        } else if (qrGenerate.getQrType() == QrScan.QRType.SMS) {
                            QrMess qrMess = new QrMess();
                            qrMess.compileSMS(content);
                            shareListener.share(qrMess.getShare());
                        } else if (qrGenerate.getQrType() == QrScan.QRType.PRODUCT) {
                            QrProduct qrProduct = new QrProduct();
                            qrProduct.compileProduct(qrGenerate.getContent());
                            shareListener.share(qrProduct.getShare());
                        } else if (qrGenerate.getQrType() == QrScan.QRType.WIFI) {
                            QrWifi qrWifi = new QrWifi();
                            StringBuilder stringBuilder = new StringBuilder();
                            String[] contentWifi = qrGenerate.getContent().split(";");
                            for (String value : contentWifi) {
                                stringBuilder.append(value);
                            }
                            String contentWifi2 = stringBuilder.toString();
                            String[] contentWifi3 = contentWifi2.split(":");
                            qrWifi.compileWifi(contentWifi, contentWifi3);
                            shareListener.share(qrWifi.getShare());
                        } else if (qrGenerate.getQrType() == QrScan.QRType.PHONE) {
                            QreTelephone qreTelephone = new QreTelephone();
                            qreTelephone.compile(content);
                            shareListener.share(qreTelephone.getShare());
                        } else if (qrGenerate.getQrType() == QrScan.QRType.TEXT) {
                            QrText qrText = new QrText();
                            shareListener.share(qrText.getShare());
                        } else if (qrGenerate.getQrType() == QrScan.QRType.URL) {
                            QrUrl qrUrl = new QrUrl();
                            qrUrl.compileUrl(content);
                            shareListener.share(qrUrl.getShare());
                        }
                    }
                }
            });
            imvItemEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editListener.edit(isEdit);
                }
            });
            imvItemDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notifyItemRemoved(getLayoutPosition());
                    deleteListener.delete(generateItemList.get(getLayoutPosition()), getLayoutPosition());
                }
            });


        }
    }

    private IEdit editListener;

    private IShare shareListener;

    public interface IShare {
        void share(String s);
    }

    private IDelete deleteListener;

    public interface IDelete {
        void delete(QrGenerate qrGenerate, int position);
    }

    public interface IEdit {
        void edit(boolean isEdit);
    }

}

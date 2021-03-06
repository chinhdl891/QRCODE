package com.example.qrscaner.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrscaner.models.QrEmail;
import com.example.qrscaner.models.QrGenerate;
import com.example.qrscaner.models.QrMess;
import com.example.qrscaner.models.QrProduct;
import com.example.qrscaner.models.QrScan;
import com.example.qrscaner.models.QrText;
import com.example.qrscaner.models.QrUrl;
import com.example.qrscaner.models.QrWifi;
import com.example.qrscaner.models.QreTelephone;
import com.example.qrscaner.R;
import com.example.qrscaner.view.fonts.TextViewPoppinBold;

import java.util.List;

public class GenerateHistoryAdapter extends RecyclerView.Adapter<GenerateHistoryAdapter.GenerateQrCodeHistoryViewHolder> {
    private List<QrGenerate> generateItemList;
    private boolean isEdit;
    private EditGenerateListener editGenerateListener;
    private ShowData showData;


    public GenerateHistoryAdapter(List<QrGenerate> generateItemList, boolean isEdit, EditGenerateListener listener, ShowData onShowDataListener) {
        this.generateItemList = generateItemList;
        this.isEdit = isEdit;
        this.editGenerateListener = listener;
        this.showData = onShowDataListener;

    }

    public void setGenerateItemList(List<QrGenerate> generateItemList) {
        this.generateItemList = generateItemList;
        notifyDataSetChanged();
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GenerateQrCodeHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_scan, parent, false);
        return new GenerateQrCodeHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenerateQrCodeHistoryViewHolder holder, int position) {
        QrGenerate qrGenerate = generateItemList.get(position);
        if (!isEdit) {
            holder.imvItemCheck.setVisibility(View.GONE);
            holder.ctlItemHistoryQrGenerate.setBackgroundResource(R.drawable.background_boder_unselect);

        } else {
            holder.imvItemCheck.setVisibility(View.VISIBLE);
            if (!qrGenerate.isEdit()){
                holder.imvItemCheck.setImageResource(R.drawable.ic_uncheck);
            }

        }
        holder.cvItemHistoryEdit.setVisibility(View.GONE);

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
            holder.imvItemScanType.setImageResource(R.drawable.ic_add_sms);
            holder.tvItemHistoryQrDate.setText(qrGenerate.getStringDate());
            if (qrGenerate.getContent().equals("")) {
                holder.tvItemHistoryQrContent.setText(R.string.sms);
            }


        } else if (content[0].equals("http") || content[0].equals("https")) {
            QrUrl qrUrl = new QrUrl();
            qrUrl.compileUrl(content);
            holder.tvItemHistoryQrContent.setText(qrUrl.getUrl());
            holder.imvItemScanType.setImageResource(R.drawable.ic_add_uri);
            holder.tvItemHistoryQrDate.setText(qrGenerate.getStringDate());

            if (qrGenerate.getContent().equals("")) {
                holder.tvItemHistoryQrContent.setText(R.string.uri);
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
            holder.imvItemScanType.setImageResource(R.drawable.ic_add_wifi);
            holder.tvItemHistoryQrDate.setText(qrGenerate.getStringDate());

            if (qrGenerate.getContent().equals("")) {
                holder.tvItemHistoryQrContent.setText(R.string.wifi);
            }
        } else if (content[0].equals("MATMSG")) {
            QrEmail qrEmail = new QrEmail();
            StringBuilder stringBuilder = new StringBuilder();
            for (String value : content) {
                stringBuilder.append(value);
            }
            qrEmail.compileEmail(content);
            holder.tvItemHistoryQrContent.setText(qrEmail.getSendBy());
            holder.imvItemScanType.setImageResource(R.drawable.ic_add_email);
            holder.tvItemHistoryQrDate.setText(qrEmail.getDateString());
            if (qrGenerate.getContent().equals("")) {
                holder.tvItemHistoryQrContent.setText(R.string.email);
            }

        } else if (content[0].equals("tel")) {

            QreTelephone qreTelephone = new QreTelephone();
            qreTelephone.compile(content);
            holder.tvItemHistoryQrContent.setText(qreTelephone.getTel());
            holder.imvItemScanType.setImageResource(R.drawable.ic_add_call);
            holder.tvItemHistoryQrDate.setText(qreTelephone.getDateString());
            if (qrGenerate.getContent().equals("")) {
                holder.tvItemHistoryQrContent.setText(R.string.phone_number);
            }

        } else {

            if (qrGenerate.getQrType() == QrScan.QRType.BAR39) {
                holder.tvItemHistoryQrContent.setText(qrGenerate.getContent());
                holder.imvItemScanType.setImageResource(R.drawable.ic_barcoder3996128);
                holder.tvItemHistoryQrDate.setText(qrGenerate.getStringDate());
                if (qrGenerate.getContent().equals("")) {
                    holder.tvItemHistoryQrContent.setText("BAR39");
                }
            } else if (qrGenerate.getQrType() == QrScan.QRType.BAR93) {
                holder.tvItemHistoryQrContent.setText(qrGenerate.getContent());
                holder.imvItemScanType.setImageResource(R.drawable.ic_barcoder3996128);
                holder.tvItemHistoryQrDate.setText(qrGenerate.getStringDate());
                if (qrGenerate.getContent().equals("")) {
                    holder.tvItemHistoryQrContent.setText("BAR93");
                }
            } else if (qrGenerate.getQrType() == QrScan.QRType.BAR128) {
                holder.tvItemHistoryQrContent.setText(qrGenerate.getContent());
                holder.imvItemScanType.setImageResource(R.drawable.ic_barcoder3996128);
                holder.tvItemHistoryQrDate.setText(qrGenerate.getStringDate());
                if (qrGenerate.getContent().equals("")) {
                    holder.tvItemHistoryQrContent.setText("BAR128");
                }
            } else {
                QrText qrText = new QrText();
                qrText.setText(qrGenerate.getContent());
                holder.tvItemHistoryQrContent.setText(qrGenerate.getContent());
                holder.imvItemScanType.setImageResource(R.drawable.ic_add_text);
                holder.tvItemHistoryQrDate.setText(qrGenerate.getStringDate());
            }
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
        private ConstraintLayout ctlItemHistoryDate, ctlItemHistoryQrGenerate;
        private TextViewPoppinBold tvItemHistoryQrContent;
        private TextView tvItemHistoryDate;
        private TextView tvItemHistoryQrDate;
        private CardView cvItemHistoryQr, cvItemHistoryEdit;
        private ImageView imvItemScanType, imvItemScanMenu, imvItemCheck, imvItemCancel;
        private TextView imvItemShare, imvItemEdit, imvItemDelete;


        public GenerateQrCodeHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ctlItemHistoryQrGenerate = itemView.findViewById(R.id.ctl_item_history__scannedBackground);
            tvItemHistoryDate = itemView.findViewById(R.id.tv_item_history_monthCreate);
            ctlItemHistoryDate = itemView.findViewById(R.id.csl_item_history__date);
            cvItemHistoryQr = itemView.findViewById(R.id.cv_item_history__scannedContainer);
            imvItemCheck = itemView.findViewById(R.id.imv_item_history__scannedCheck);
            tvItemHistoryQrContent = itemView.findViewById(R.id.tv_item_history__scannedContent);
            tvItemHistoryQrDate = itemView.findViewById(R.id.tv_item_history__scannedDate);
            imvItemScanMenu = itemView.findViewById(R.id.imv_item_history__scannedMenu);
            imvItemScanType = itemView.findViewById(R.id.imv_item_history__scannedType);
            cvItemHistoryEdit = itemView.findViewById(R.id.cv_item_history__editContainer);
            imvItemCancel = itemView.findViewById(R.id.imv_item_history__editCancel);
            imvItemDelete = itemView.findViewById(R.id.txv_item_history__editDelete);
            imvItemEdit = itemView.findViewById(R.id.txv_item_history__edit);
            imvItemShare = itemView.findViewById(R.id.txv_item_history__editShare);

            imvItemScanMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cvItemHistoryQr.setVisibility(View.GONE);
                    cvItemHistoryEdit.setVisibility(View.VISIBLE);
                }
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
                            editGenerateListener.onShareGenerate(qrGenerate.getContent(), QrScan.QRType.TEXT, qrGenerate.getColor());
                        } else if (qrGenerate.getQrType() == QrScan.QRType.SMS) {
                            QrMess qrMess = new QrMess();
                            qrMess.compileSMS(content);
                            editGenerateListener.onShareGenerate(qrMess.getShare(), QrScan.QRType.SMS, qrGenerate.getColor());
                        } else if (qrGenerate.getQrType() == QrScan.QRType.PRODUCT) {
                            QrProduct qrProduct = new QrProduct();
                            qrProduct.compileProduct(qrGenerate.getContent());
                            editGenerateListener.onShareGenerate(qrGenerate.getContent(), QrScan.QRType.TEXT, qrGenerate.getColor());
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
                            editGenerateListener.onShareGenerate(qrGenerate.getContent(), QrScan.QRType.TEXT, qrGenerate.getColor());
                        } else if (qrGenerate.getQrType() == QrScan.QRType.PHONE) {
                            QreTelephone qreTelephone = new QreTelephone();
                            qreTelephone.compile(content);
                            editGenerateListener.onShareGenerate(qrGenerate.getContent(), QrScan.QRType.TEXT, qrGenerate.getColor());
                        } else if (qrGenerate.getQrType() == QrScan.QRType.TEXT) {
                            QrText qrText = new QrText();
                            editGenerateListener.onShareGenerate(qrGenerate.getContent(), QrScan.QRType.TEXT, qrGenerate.getColor());
                        } else if (qrGenerate.getQrType() == QrScan.QRType.URL) {
                            QrUrl qrUrl = new QrUrl();
                            qrUrl.compileUrl(content);
                            editGenerateListener.onShareGenerate(qrGenerate.getContent(), QrScan.QRType.TEXT, qrGenerate.getColor());
                        } else if (qrGenerate.getQrType() == QrScan.QRType.BAR39) {
                            editGenerateListener.onShareGenerate(qrGenerate.getContent(), QrScan.QRType.BAR39, qrGenerate.getColor());
                        } else if (qrGenerate.getQrType() == QrScan.QRType.BAR93) {
                            editGenerateListener.onShareGenerate(qrGenerate.getContent(), QrScan.QRType.BAR93, qrGenerate.getColor());
                        } else if (qrGenerate.getQrType() == QrScan.QRType.BAR128) {
                            editGenerateListener.onShareGenerate(qrGenerate.getContent(), QrScan.QRType.BAR128, qrGenerate.getColor());
                        }
                    }
                }
            });
            imvItemEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    editGenerateListener.onEditGenerate(!isEdit);
                }
            });
            imvItemDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editGenerateListener.onDeleteGenerate(generateItemList.get(getLayoutPosition()), getLayoutPosition());
                    notifyItemRemoved(getLayoutPosition());
                    notifyDataSetChanged();
                }
            });


            cvItemHistoryQr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    QrGenerate qrGenerate = generateItemList.get(getLayoutPosition());
                    if (isEdit) {
                        if (!qrGenerate.isEdit()) {
                            qrGenerate.setEdit(true);
                            ctlItemHistoryQrGenerate.setBackgroundResource(R.drawable.background_boder_selected);
                            imvItemCheck.setImageResource(R.drawable.ic_check);
                            editGenerateListener.onSelectedItem(true);
                        } else {
                            qrGenerate.setEdit(false);
                            ctlItemHistoryQrGenerate.setBackgroundResource(R.drawable.background_boder_unselect);
                            imvItemCheck.setImageResource(R.drawable.ic_uncheck);
                            editGenerateListener.onSelectedItem(false);
                        }

                    } else {
                        showData.onShowListener(qrGenerate);

                    }
                }
            });



        }
    }

    public interface EditGenerateListener {
        void onShareGenerate(String s, QrScan.QRType type, int color);

        void onDeleteGenerate(QrGenerate qrGenerate, int i);

        void onEditGenerate(boolean isEdit);

        void onSelectedItem(boolean isSelect);

    }

    public interface ShowData {
        void onShowListener(QrGenerate qrGenerate);
    }
}

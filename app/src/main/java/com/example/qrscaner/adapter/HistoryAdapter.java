package com.example.qrscaner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrscaner.Model.QrEmail;
import com.example.qrscaner.Model.QrMess;
import com.example.qrscaner.Model.QrScan;
import com.example.qrscaner.Model.QrUrl;
import com.example.qrscaner.Model.QrWifi;
import com.example.qrscaner.Model.QreTelephone;
import com.example.qrscaner.R;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.internal.markers.KMutableListIterator;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {
    private final List<QrScan> qrList;
    private boolean isEdit = false;
    private final Context mContext;
    private HistoryAdapterListener mHistoryAdapterListener;


    public HistoryAdapter(Context mContext, List<QrScan> qrList, HistoryAdapterListener listener) {
        this.qrList = qrList;
        this.mContext = mContext;
        mHistoryAdapterListener = listener;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_scan, parent, false);
        return new HistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
        QrScan qrScan = qrList.get(position);
        holder.tvItemHistoryScannedDate.setText(qrScan.getDateString());
        if (position > 0) {
            QrScan qrScanUndo = qrList.get(position - 1);
            if (qrScanUndo.getDateString().equals(qrScan.getDateString())) {
                holder.cslItemHistoryDate.setVisibility(View.GONE);
            }
        } else {
            holder.cslItemHistoryDate.setVisibility(View.VISIBLE);
        }
        String[] content = qrScan.getScanText().split(":");
        if (content[0].equals("SMSTO")) {
            QrMess qrMess = new QrMess();
            qrMess.compileSMS(content);
            holder.tvItemHistoryScannedContent.setText(qrMess.getSendBy());
            holder.imvItemHistoryScannedType.setImageResource(R.drawable.add_sms);
            holder.tvItemHistoryScannedDate.setText(qrScan.getDateString());
            if (qrScan.getScanText().equals("")) {
                holder.tvItemHistoryScannedContent.setText("SMS");
            }

        } else if (content[0].equals("Error")) {
            holder.tvItemHistoryScannedContent.setText("Error");
            holder.imvItemHistoryScannedType.setImageResource(R.drawable.ic_error);
            holder.tvItemHistoryScannedDate.setText(qrScan.getDateString());
            if (qrScan.getScanText().equals("")) {
                holder.tvItemHistoryScannedContent.setText("ERROR");
            }

        } else if (content[0].equals("http") || content[0].equals("https")) {
            QrUrl qrUrl = new QrUrl();
            qrUrl.compileUrl(content);
            holder.tvItemHistoryScannedContent.setText(qrUrl.getUrl());
            holder.imvItemHistoryScannedType.setImageResource(R.drawable.add_uri);
            holder.tvItemHistoryScannedDate.setText(qrScan.getDateString());
            if (qrScan.getScanText().equals("")) {
                holder.tvItemHistoryScannedContent.setText("Uri");
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
            holder.tvItemHistoryScannedContent.setText(qrWifi.getWifiName());
            holder.imvItemHistoryScannedType.setImageResource(R.drawable.add_wifi);
            holder.tvItemHistoryScannedDate.setText(qrScan.getDateString());

            if (qrScan.getScanText().equals("")) {
                holder.tvItemHistoryScannedContent.setText("Wifi");
            }
        } else if (content[0].equals("MATMSG")) {
            QrEmail qrEmail = new QrEmail();
            StringBuilder stringBuilder = new StringBuilder();
            for (String value : content) {
                stringBuilder.append(value);
            }
            qrEmail.compileEmail(content);
            holder.tvItemHistoryScannedContent.setText(qrEmail.getSendBy());
            holder.imvItemHistoryScannedType.setImageResource(R.drawable.add_email);
            holder.tvItemHistoryScannedDate.setText(qrScan.getDateString());
            if (qrScan.getScanText().equals("")) {
                holder.tvItemHistoryScannedContent.setText("Email");
            }
        } else if (content[0].equals("tel")) {
            QreTelephone qreTelephone = new QreTelephone();
            qreTelephone.compile(content);
            holder.tvItemHistoryScannedContent.setText(qreTelephone.getTel());
            holder.imvItemHistoryScannedType.setImageResource(R.drawable.add_call);
            holder.tvItemHistoryScannedDate.setText(qrScan.getDateString());
            if (qrScan.getScanText().equals("")) {
                holder.tvItemHistoryScannedContent.setText("Phone");
            }

        } else {
            if (checkIsProduct(qrScan.getScanText())) {
                holder.tvItemHistoryScannedContent.setText(qrScan.getScanText());
                holder.imvItemHistoryScannedType.setImageResource(R.drawable.ic_product);
                holder.tvItemHistoryScannedDate.setText(qrScan.getDateString());
                if (qrScan.getScanText().equals("")) {
                    holder.tvItemHistoryScannedContent.setText("Product");
                }
            } else {
                holder.tvItemHistoryScannedContent.setText(qrScan.getScanText());
                holder.imvItemHistoryScannedType.setImageResource(R.drawable.add_text);
                holder.tvItemHistoryScannedDate.setText(qrScan.getDateString());
                if (qrScan.getScanText().equals("")) {
                    holder.tvItemHistoryScannedContent.setText("Text");
                }
            }
        }
        if (isEdit) {
            holder.imvItemHistoryScannedCheck.setVisibility(View.VISIBLE);
            if (qrScan.getIsChecked()) {
                holder.imvItemHistoryScannedCheck.setImageResource(R.drawable.ic_check);
                holder.ctlItemHistoryScannedBackground.setBackgroundResource(R.drawable.background_boder_selected);

            } else {

                holder.imvItemHistoryScannedCheck.setImageResource(R.drawable.ic_uncheck);
                holder.ctlItemHistoryScannedBackground.setBackgroundResource(R.drawable.background_boder_unselect);
            }
        } else {
            holder.imvItemHistoryScannedCheck.setVisibility(View.GONE);
            holder.ctlItemHistoryScannedBackground.setBackgroundResource(R.drawable.background_boder_unselect);
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

    public class HistoryHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout cslItemHistoryDate;
        private final TextView tvItemHistoryCreate;
        private final TextView tvItemHistoryMonthCreate;
        private final CardView cvItemHistoryScannedContainer;
        private final ConstraintLayout ctlItemHistoryScannedBackground;
        private final ImageView imvItemHistoryScannedType;
        private final TextView tvItemHistoryScannedContent;
        private final TextView tvItemHistoryScannedDate;
        private final ImageView imvItemHistoryScannedMenu;
        private final ImageView imvItemHistoryScannedCheck;
        private final CardView cvItemHistoryEditContainer;
        private final ImageButton imvItemHistoryEditCancel;
        private final TextView txvItemHistoryEdit;
        private final TextView txvItemHistoryEditShare;
        private final TextView txvItemHistoryEditDelete;

        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            cslItemHistoryDate = (ConstraintLayout) itemView.findViewById(R.id.csl_item_history__date);
            tvItemHistoryCreate = (TextView) itemView.findViewById(R.id.tv_item_history_create);
            tvItemHistoryMonthCreate = (TextView) itemView.findViewById(R.id.tv_item_history_monthCreate);
            cvItemHistoryScannedContainer = (CardView) itemView.findViewById(R.id.cv_item_history__scannedContainer);
            ctlItemHistoryScannedBackground = (ConstraintLayout) itemView.findViewById(R.id.ctl_item_history__scannedBackground);
            imvItemHistoryScannedType = (ImageView) itemView.findViewById(R.id.imv_item_history__scannedType);
            tvItemHistoryScannedContent = (TextView) itemView.findViewById(R.id.tv_item_history__scannedContent);
            tvItemHistoryScannedDate = (TextView) itemView.findViewById(R.id.tv_item_history__scannedDate);
            imvItemHistoryScannedMenu = (ImageView) itemView.findViewById(R.id.imv_item_history__scannedMenu);
            imvItemHistoryScannedCheck = (ImageView) itemView.findViewById(R.id.imv_item_history__scannedCheck);
            cvItemHistoryEditContainer = (CardView) itemView.findViewById(R.id.cv_item_history__editContainer);
            imvItemHistoryEditCancel = (ImageButton) itemView.findViewById(R.id.imv_item_history__editCancel);
            txvItemHistoryEdit = (TextView) itemView.findViewById(R.id.txv_item_history__edit);
            txvItemHistoryEditShare = (TextView) itemView.findViewById(R.id.txv_item_history__editShare);
            txvItemHistoryEditDelete = (TextView) itemView.findViewById(R.id.txv_item_history__editDelete);

            txvItemHistoryEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setEdit(true);
                    if (mHistoryAdapterListener != null) {
                        mHistoryAdapterListener.onEditHistory(true);
                    }
                }
            });

            imvItemHistoryEditCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cvItemHistoryEditContainer.setVisibility(View.GONE);
                }
            });

            txvItemHistoryEditDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mHistoryAdapterListener != null) {
                        mHistoryAdapterListener.onDeleteQRSelected(qrList.get(getLayoutPosition()));
                        notifyItemRemoved(getLayoutPosition());
                    }
                }
            });

            imvItemHistoryScannedMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cvItemHistoryEditContainer.setVisibility(View.VISIBLE);
                }
            });

            txvItemHistoryEditShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    QrScan qrScan = qrList.get(getLayoutPosition());
                    if (mHistoryAdapterListener != null) {
                        mHistoryAdapterListener.onShareQRSelected(qrScan);
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isEdit) {
                        boolean isSelect = !qrList.get(getLayoutPosition()).isChecked();
                        qrList.get(getLayoutPosition()).setEdit(isSelect);
                        notifyItemChanged(getLayoutPosition());
                        if (mHistoryAdapterListener != null) {
                            mHistoryAdapterListener.onItemSelected(isSelect);
                        }
                    } else {
                        //show detail QR code
                    }
                }
            });

        }

    }

    public interface HistoryAdapterListener {

        void onShareQRSelected(QrScan qrCode);

        void onEditHistory(boolean isEdit);

        void onDeleteQRSelected(QrScan qrScan);

        void onItemSelected(boolean isSelected);


    }


}

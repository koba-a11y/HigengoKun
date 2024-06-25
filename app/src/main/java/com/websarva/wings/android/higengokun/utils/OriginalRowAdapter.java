package com.websarva.wings.android.higengokun.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.websarva.wings.android.higengokun.R;

import java.util.List;
import java.util.Map;

public class OriginalRowAdapter extends BaseAdapter {
    private final Context context;
    private final List<Map<String, String>> data;
    private final LayoutInflater inflater;

    public OriginalRowAdapter(Context context, List<Map<String, String>> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.category_row, parent, false);
            holder = new ViewHolder();
            holder.tvCategoryRow = convertView.findViewById(R.id.tvCategoryRow);
            holder.tvQuestionRow = convertView.findViewById(R.id.tvQuestionRow);
            holder.tvAnswerFeedback = convertView.findViewById(R.id.tvAnswerFeedback);
            holder.ivCategoryIcon = convertView.findViewById(R.id.ivCategoryIcon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Map<String, String> item = data.get(position);
        holder.tvCategoryRow.setText(item.get("category"));
        holder.tvQuestionRow.setText(item.get("question"));

        // tvTrueFalseの表示を制御する
        if (item.containsKey("feedback")) {
            holder.tvAnswerFeedback.setVisibility(View.VISIBLE);
            holder.tvAnswerFeedback.setText(item.get("feedback"));
        } else {
            holder.tvAnswerFeedback.setVisibility(View.GONE);
        }

        // カテゴリに応じた画像を設定する
        switch (item.get("category")) {
            case "場合の数:":
                holder.ivCategoryIcon.setImageResource(R.drawable.ic_baai); // ic_baai はあなたの画像リソース
                break;
            case "濃度:":
                holder.ivCategoryIcon.setImageResource(R.drawable.ic_noudo); // ic_noudo はあなたの画像リソース
                break;
            case "確率:":
                holder.ivCategoryIcon.setImageResource(R.drawable.ic_kakuritu); // ic_kakuritu はあなたの画像リソース
                break;
            case "損益:":
                holder.ivCategoryIcon.setImageResource(R.drawable.ic_soneki); // ic_soneki はあなたの画像リソース
                break;
            case "代金の清算:":
                holder.ivCategoryIcon.setImageResource(R.drawable.ic_daikin); // ic_daikin はあなたの画像リソース
                break;
            default:
                holder.ivCategoryIcon.setImageResource(R.drawable.maesentouk); // ic_default はデフォルトの画像リソース
                break;
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView tvCategoryRow;
        TextView tvQuestionRow;
        TextView tvAnswerFeedback;
        ImageView ivCategoryIcon;
    }
}

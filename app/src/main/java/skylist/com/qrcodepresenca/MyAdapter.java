package skylist.com.qrcodepresenca;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<CheckDoDia> presenceList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView materia, day, preceptor;

        public MyViewHolder(View view) {
            super(view);
            materia     = view.findViewById(R.id.textMateria);
            day         = view.findViewById(R.id.textDay);
            preceptor   = view.findViewById(R.id.textPreceptor);
        }
    }

    public MyAdapter(List<CheckDoDia> presenceList) {
        this.presenceList = presenceList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.presence_list_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CheckDoDia prsc = presenceList.get(position);
        holder.materia.setText(prsc.materia);
        holder.day.setText(prsc.date);
        holder.preceptor.setText(prsc.preceptor);
    }

    @Override
    public int getItemCount() {
        return presenceList.size();
    }

}

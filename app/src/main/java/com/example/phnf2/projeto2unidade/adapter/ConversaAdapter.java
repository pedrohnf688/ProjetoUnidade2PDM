package com.example.phnf2.projeto2unidade.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.phnf2.projeto2unidade.R;
import com.example.phnf2.projeto2unidade.modelo.Conversa;

import java.util.List;

public class ConversaAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<Conversa> listaConversa;

    public ConversaAdapter(Context context, List<Conversa> listaConversa) {
        this.context = context;
        this.listaConversa = listaConversa;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.inflate_mensagem, viewGroup, false);
        ConversaViewHolder cv = new ConversaViewHolder(view);

        return cv;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int posicao) {

        ConversaViewHolder holder = (ConversaViewHolder) viewHolder;

        Conversa conversaEscolhida = listaConversa.get(posicao);

        boolean isPhoto = conversaEscolhida.getPhotoUrl() != null;

        if(isPhoto){
            holder.TextMensagem.setVisibility(View.GONE);
            holder.ImgLoad.setVisibility(View.VISIBLE);
            Glide.with(holder.ImgLoad.getContext()).load(conversaEscolhida.getPhotoUrl()).into(holder.ImgLoad);
        }else{
            holder.TextMensagem.setVisibility(View.VISIBLE);
            holder.ImgLoad.setVisibility(View.GONE);
            holder.TextMensagem.setText(conversaEscolhida.getMensagem());
        }

        holder.TextNome.setText(conversaEscolhida.getNome());
        holder.TextData.setText(""+conversaEscolhida.getData());





    }

    @Override
    public int getItemCount() {
        return listaConversa.size();
    }


    public class ConversaViewHolder extends RecyclerView.ViewHolder {

        final TextView TextNome;
        final TextView TextMensagem;
        final TextView TextData;
        final ImageView ImgLoad;

        public ConversaViewHolder(View itemView) {
            super(itemView);

            TextNome = itemView.findViewById(R.id.Nome);
            TextMensagem = itemView.findViewById(R.id.Mensagem);
            TextData = itemView.findViewById(R.id.data);
            ImgLoad = itemView.findViewById(R.id.imgLoad);
        }
    }

}

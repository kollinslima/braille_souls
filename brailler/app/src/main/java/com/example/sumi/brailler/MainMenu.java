package com.example.sumi.brailler;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.sumi.brailler.database.DataBaseHelper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainMenu extends AppCompatActivity {

    public static final int[] PADRAO_ANIMACAO = {
            R.drawable.ic_background_animation_1,
            R.drawable.ic_background_animation_2,
            R.drawable.ic_background_animation_3,
            R.drawable.ic_background_animation_4,
            R.drawable.ic_background_animation_5,
            R.drawable.ic_background_animation_6
    };

    public static final long TEMPO_ANIMACAO = 5000;

    public static final String DATABASE_TAG = "DatabaseTest";
    public static final HashMap<String, String> text_to_braille = new HashMap<String, String>();
    public static final Multimap<String, String> braille_to_text = ArrayListMultimap.create();

    private FrameLayout frameAnimacao;
    private ImageView auxImageView;
    private ArrayList<ImageView> vetorAnimacao;
    private boolean iniciaAnimacao;

    private ValueAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal);

        inicializaTradutor();

        frameAnimacao = (FrameLayout) findViewById(R.id.frameAnimacao);
        vetorAnimacao = new ArrayList<ImageView>();
        iniciaAnimacao = true;

        //Adiciona primeiro elemento para poder pegar o tamanho
        auxImageView = new ImageView(getContext());
        auxImageView.setImageResource(PADRAO_ANIMACAO[0]);
        auxImageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        auxImageView.setTranslationY(0);

        vetorAnimacao.add(0, auxImageView);

        frameAnimacao.addView(auxImageView);

        animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(TEMPO_ANIMACAO);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float heightFrame = frameAnimacao.getHeight();
                final float heightBase = auxImageView.getHeight();
                final float translationY = (heightBase*PADRAO_ANIMACAO.length) * progress;

                Log.d("Animacao", "Tranlation: " + translationY);

                if (iniciaAnimacao) {
                    if (heightBase > 0) {
                        Log.d("Animacao", "Base: " + heightBase);

                        //Posiciona acima da tela para rolagem
                        vetorAnimacao.get(0).setTranslationY(-PADRAO_ANIMACAO.length*heightBase);

                        //Preenche a tela com o padrão
                        for (int i = 1; (i - PADRAO_ANIMACAO.length) * heightBase < heightFrame; i++) {
                            auxImageView = new ImageView(getContext());
                            auxImageView.setImageResource(PADRAO_ANIMACAO[i % 6]);
                            auxImageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            auxImageView.setTranslationY((i - PADRAO_ANIMACAO.length) * heightBase);

                            vetorAnimacao.add(i, auxImageView);

                            frameAnimacao.addView(auxImageView);
                        }

                        iniciaAnimacao = false;
                    }
                } else {

                    for (int i = 0; i < vetorAnimacao.size(); i++) {
                        vetorAnimacao.get(i).setTranslationY(translationY + (i-PADRAO_ANIMACAO.length)*heightBase);
                    }

                }

//                animacao[0].setTranslationY(translationY);
//                animacao[1].setTranslationY(translationY - heightBase);
//                animacao[2].setTranslationY(translationY - 2*heightBase);
//                animacao[3].setTranslationY(translationY - 3*heightBase);
//                animacao[4].setTranslationY(translationY - 4*heightBase);
//                animacao[5].setTranslationY(translationY - 5*heightBase);

            }
        });
        animator.start();

    }

    private void inicializaTradutor() {

        DataBaseHelper mDBHelper = new DataBaseHelper(this);
        SQLiteDatabase mDb = null;

        try {
            mDBHelper.updateDataBase();
            mDb = mDBHelper.getWritableDatabase();

            Cursor cursor = mDb.query("brailleTable", null, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                do {

                    text_to_braille.put(cursor.getString(cursor.getColumnIndex("PlainText")),
                            cursor.getString(cursor.getColumnIndex("Braille")));

                    braille_to_text.put(cursor.getString(cursor.getColumnIndex("Braille")),
                            cursor.getString(cursor.getColumnIndex("PlainText")));

//                    Log.d(DATABASE_TAG, "Braille: " + cursor.getString(cursor.getColumnIndex("Braille"))
//                            + " - Text: " + braille_to_text.get(cursor.getString(cursor.getColumnIndex("Braille"))));
                } while (cursor.moveToNext());
            }

        } catch (SQLException mSQLException) {
            throw mSQLException;
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        } finally {
            mDb.close();
        }
    }

    public void onClickStartButton(View view) {
        Intent intent = new Intent(this, MainGame.class);
        startActivity(intent);
    }

    public void onClickDictionatyButton(View view) {
        Intent intent = new Intent(this, TradutorParaBraille.class);
        startActivity(intent);
    }

    public void onClickExitButton(View view) {
        finish();
    }

    private Context getContext() {
        return this;
    }
}

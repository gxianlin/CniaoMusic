package com.cainiao.music.cniaomusic.ui.cnmusic;

import android.view.View;
import android.widget.TextView;

import com.cainiao.music.cniaomusic.R;
import com.cainiao.music.cniaomusic.ui.base.BaseAvtivity;
import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchHistoryTable;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：搜索
 * 作者：gong.xl
 * 创建日期：2017/7/5 下午10:43
 * 修改日期: 2017/7/3
 * 修改备注：
 * 邮箱：gxianlin@126.com
 */
public abstract class SearchActivity extends BaseAvtivity {
    protected SearchHistoryTable mHistoryTable;
    protected SearchView searchView = null;

    protected void setSearchView(){
        mHistoryTable = new SearchHistoryTable(this);
        searchView = (SearchView) findViewById(R.id.searchView);
        if (searchView != null) {
            searchView.setHint(R.string.search_hint);
            searchView.setVoiceText("Set permission on Android 6+ !");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    getData(query);
                    searchView.close(true);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

            searchView.setOnVoiceClickListener(new SearchView.OnVoiceClickListener() {
                @Override
                public void onVoiceClick() {
                }
            });

            if (searchView.getAdapter() == null) {
                List<SearchItem> suggestionsList = new ArrayList<>();

                SearchAdapter searchAdapter = new SearchAdapter(this, suggestionsList);
                searchAdapter.addOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                        String query = textView.getText().toString();
                        getData(query);
                        searchView.close(true);
                    }
                });
                searchView.setAdapter(searchAdapter);
            }


        }
    }

    private void getData(String query) {

    }

    protected void customSearchView(boolean menuItem){
        if (searchView != null){
            if (menuItem){
                searchView.setVersion(SearchView.VERSION_MENU_ITEM);
                searchView.setVersionMargins(SearchView.VERSION_MARGINS_MENU_ITEM);
                searchView.setTheme(SearchView.THEME_LIGHT);
            }else {
                searchView.setVersion(SearchView.VERSION_TOOLBAR);
                searchView.setVersionMargins(SearchView.VERSION_MARGINS_TOOLBAR_SMALL);
                searchView.setTheme(SearchView.THEME_LIGHT);
            }
        }
    }
}

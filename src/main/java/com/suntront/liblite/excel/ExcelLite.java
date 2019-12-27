/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2019, Jeek
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.suntront.liblite.excel;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;

/**
 * 作者: Jeek.li
 * 时间: 2018/10/22
 * <p>
 * 描述:获取本地Excel中数据并插入SQLite数据库
 * 使用: new ExcelLite(context).execute("meters.xls");
 */
public class ExcelLite extends AsyncTask<String, Void, ArrayList<String>> {

    public final static String TAG = "ExcelLite";
    //    protected DataSource mDataSource = App.INSTANCE.dataSource;
    private Context context;

    public ExcelLite(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
//        progressDialog = ProgressDialog.create(context, "", "正在读取Excel数据...");
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        return getXlsData(params[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<String> Strings) {
//        if (progressDialog.isShowing()) {
//            progressDialog.dismiss();
//        }

        if (Strings != null && Strings.size() > 0) {
            //存在数据
//            sortByName(Strings);
//            setupData(Strings);
        } else {
            //加载失败


        }

    }


    /**
     * 获取 excel 表格中的数据,不能在主线程中调用
     *
     * @param xlsName excel 表格的名称
     */
    private ArrayList<String> getXlsData(String xlsName) {
        ArrayList<String> countryList = new ArrayList<String>();
        AssetManager assetManager = context.getAssets();

        try {

            Workbook workbook = Workbook.getWorkbook(assetManager.open(xlsName));
            Sheet sheet = workbook.getSheet(0);

            int sheetNum = workbook.getNumberOfSheets();
            int sheetRows = sheet.getRows();
            int sheetColumns = sheet.getColumns();

            Log.d(TAG, "jeek the num of sheets is " + sheetNum);
            Log.d(TAG, "jeek the name of sheet is  " + sheet.getName());
            Log.d(TAG, "jeek total rows is 行=" + sheetRows);
            Log.d(TAG, "jeek total cols is 列=" + sheetColumns);

            //向ArrayList中装载数据
            for (int i = 0; i < sheetRows; i++) {
//                String String = new String();
//                String.setChinaName(sheet.getCell(0, i).getContents());
//                String.setEnglishName(sheet.getCell(1, i).getContents());
//                String.setAreaNumber(sheet.getCell(2, i).getContents());

                // 向SQLite中插入数据
                // mDataSource.databaseSource.saveString(String);
//                countryList.add(String);
            }

            workbook.close();

        } catch (Exception e) {
            Log.e(TAG, "jeek framework Excel read error=" + e, e);
        }

        return countryList;
    }
}

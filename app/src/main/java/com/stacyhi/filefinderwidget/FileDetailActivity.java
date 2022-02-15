package com.stacyhi.filefinderwidget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.FileUtils;


public class FileDetailActivity extends AppCompatActivity {
    ImageView iv_file_detail;
    TextView tv_file_detail_open, tv_file_detail_share, tv_file_detail_name, tv_file_detail_path, tv_file_detail_modified, tv_file_detail_type, tv_file_detail_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_detail);

        if (PermissionManager.checkPermissions(this, PermissionManager.permissionsStorage)) {
            iv_file_detail = findViewById(R.id.iv_file_detail);
            tv_file_detail_open = findViewById(R.id.tv_file_detail_open);
            tv_file_detail_share = findViewById(R.id.tv_file_detail_share);
            tv_file_detail_name = findViewById(R.id.tv_file_detail_name);
            tv_file_detail_path = findViewById(R.id.tv_file_detail_path);
            tv_file_detail_modified = findViewById(R.id.tv_file_detail_modified);
            tv_file_detail_type = findViewById(R.id.tv_file_detail_type);
            tv_file_detail_size = findViewById(R.id.tv_file_detail_size);

            DateTimeFormatter dtfYearMonthDayTime = DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a");

            String filePath = getIntent().getStringExtra("filePath");
            if (filePath != null) {
                File file = new File(filePath);
                FileDetail fileDetail = new FileDetail(this, file);

                FileImage fileImage = fileDetail.getImage();

                if (fileImage.getBitmap() != null) {
                    iv_file_detail.setImageBitmap(fileImage.getBitmap());
                } else {
                    iv_file_detail.setImageResource(fileImage.getDrawable());
                }

                tv_file_detail_name.setText(file.getName());
                tv_file_detail_path.setText(file.getAbsolutePath());
                tv_file_detail_modified.setText(String.format("Modified: %s", dtfYearMonthDayTime.format(Instant.ofEpochMilli(file.lastModified()).atZone(ZoneId.systemDefault()))));
                tv_file_detail_type.setText( String.format("Type: %s",fileDetail.getType()));
                tv_file_detail_size.setText(String.format("Size: %s ", FileUtils.byteCountToDisplaySize(file.length())));

                tv_file_detail_open.setOnClickListener(v -> {
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(fileDetail.getUri(), fileDetail.getType());
                    startActivity(intent);
                });

                tv_file_detail_share.setOnClickListener(v -> {
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType(fileDetail.getType());
                    intent.putExtra(Intent.EXTRA_STREAM, fileDetail.getUri());
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Sent from FileFinderWidget");
                    startActivity(intent);
                });

            } else {
                Toast.makeText(this, "Error finding file", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.add_permissions), Toast.LENGTH_SHORT).show();
        }
    }
}
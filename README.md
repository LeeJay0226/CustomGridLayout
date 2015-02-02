# CustomGridLayout
## Description
自定义的九宫格布局，可以将相邻宫格合并成为新的宫格

## Demo
![](https://github.com/LeeJay0226/CustomGridLayout/edit/master/images/Screenshot.png)

## Usage

<com.leejay.customgridlayout.GridLayout
        android:layout_width="match_parent"
        android:layout_height="240dp"
        android:layout_marginTop="30dp"
        android:padding="10dp"
        android:background="@android:color/white"
        app:gridlayout_columns="3"
        app:gridlayout_rows="2"
        app:gridlayout_gaps="1dp"
        app:gridlayout_orientation="vertical">

        <ImageView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@drawable/img1"
            android:scaleType="centerCrop"
            app:gridItem_rowSpan="1"
            app:gridItem_colSpan="1" />

        <ImageView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@drawable/img2"
            android:scaleType="centerCrop"
            app:gridItem_rowSpan="1"
            app:gridItem_colSpan="1" />

        <ImageView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@drawable/img3"
            android:scaleType="centerCrop"
            app:gridItem_rowSpan="2"
            app:gridItem_colSpan="2" />
</com.leejay.customgridlayout.GridLayout>


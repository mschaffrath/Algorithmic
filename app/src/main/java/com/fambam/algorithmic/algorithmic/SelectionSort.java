package com.fambam.algorithmic.algorithmic;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;

public class SelectionSort extends ArrayAlgorithm implements Parcelable {
    private int size;
    private int i_index;
    private int j_index;
    private int i_image;
    private int j_image;
    private int min_index;
    private boolean is_sorted = false;
    private boolean is_swap_phase = false;
    private boolean swapped = false;

    public SelectionSort() { super(); }

    @Override
    public void initialize(AppCompatActivity parent, ConstraintSet baseSet,
                           int[] imageIds, int[] dataIds, int[] data) {
        super.initialize(parent, baseSet, imageIds, dataIds, data);
        i_image = imageIds[0];
        j_image = imageIds[1];
        i_index = 0;
        j_index = 1;
        min_index = 0;
        size = this.data.length;
        this.updateIndicies(baseSet);
    }

    public void next(ConstraintSet set) {
        // Condition for outer loop to end if encased in loop.
        // if (i_index == imageIds.length) { return; }

        // Swap at the end of each inner loop once the smallest value's
        // index is found.
        if (is_swap_phase) {
            swapData(i_index, min_index);
            swapDataIds(i_index, min_index);
            is_swap_phase = false;
            swapped = true;
            set.createHorizontalChain(
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.LEFT,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.RIGHT,
                    this.dataIds,
                    null,
                    ConstraintSet.CHAIN_SPREAD
            );
        }
        else if (swapped) {
            i_index++;
            is_sorted = i_index == dataIds.length - 1;
            if (hasNext()) {
                j_index = i_index + 1;
                min_index = i_index;
            }
            swapped = false;
        }

        else {
            if (getDataAt(j_index) < getDataAt(min_index)) {
                min_index = j_index;
            }
            is_swap_phase = j_index == dataIds.length - 1;
            if (!is_swap_phase) {
                j_index++;
            }
        }
        updateIndicies(set);
    }

    public boolean hasNext() {
        return !is_sorted;
    }

    private void updateIndicies(ConstraintSet currentSet) {
        updateIndex(currentSet, i_image, getDataIdAt(i_index));
        if (is_sorted) {
            updateIndex(currentSet, j_image, i_image);
        }
        else {
            updateIndex(currentSet, j_image, getDataIdAt(j_index));
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeIntArray(imageIds);
        out.writeIntArray(data);
        out.writeInt(size);
        out.writeInt(i_index);
        out.writeInt(j_index);
        out.writeInt(min_index);
        out.writeInt((is_sorted ? 1 : 0));
    }

    public static final Parcelable.Creator<SelectionSort> CREATOR =
            new Parcelable.Creator<SelectionSort>() {
        public SelectionSort createFromParcel(Parcel in) {
            return new SelectionSort(in);
        }

        public SelectionSort[] newArray(int size) {
            return new SelectionSort[size];
        }
    };

    private SelectionSort(Parcel in) {
        imageIds = in.createIntArray();
        data = in.createIntArray();
        size = in.readInt();
        i_index = in.readInt();
        j_index = in.readInt();
        min_index = in.readInt();
        is_sorted = (in.readInt() == 1);
    }
}

package com.readingcards.cards;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.readingcards.R;
import com.readingcards.cards.adapter.SwipeCardsAdapter;
import com.readingcards.data.domain.CardCollection;
import com.readingcards.data.domain.Note;
import com.readingcards.util.CircleTimerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import link.fls.swipestack.SwipeStack;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by gabrielaradu on 15/12/2016.
 */
public class ReadNotesAsCardsFragment extends Fragment implements ReadNotesAsCardContract.View, SwipeStack.SwipeStackListener, View.OnClickListener {

    private ReadNotesAsCardContract.Presenter presenter;

    private Button refreshCardsBtn;
    private FloatingActionButton timerFab;
    private CircleTimerView circleTimerView;
    private TextView timerTxt;
    private RelativeLayout bottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private Button cancelTimerBtn;
    private SwipeStack swipeStack;
    private SwipeCardsAdapter adapter;
    private CountDownTimer timer;

    public static ReadNotesAsCardsFragment newInstance(final CardCollection collectionToShow) {
        Bundle arguments = new Bundle();
        arguments.putParcelable("cardCollection", collectionToShow);
        ReadNotesAsCardsFragment fragment = new ReadNotesAsCardsFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.read_notes_as_cards_frag, container, false);

        swipeStack = (SwipeStack) root.findViewById(R.id.swipe_stack);
        refreshCardsBtn = (Button) root.findViewById(R.id.refresh_cards_btn);

        circleTimerView = (CircleTimerView) root.findViewById(R.id.circle_timer_view);
        bottomSheet = (RelativeLayout) root.findViewById(R.id.linear_layout_bottom_sheet);
        timerFab = (FloatingActionButton) getActivity().findViewById(R.id.timer_fab);
        timerTxt = (TextView) root.findViewById(R.id.timer_text_view);
        cancelTimerBtn = (Button) root.findViewById(R.id.cancel_timer_btn);

        // set behaviour of bottom sheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        // Populate notes data
        CardCollection cardCollectionToShow = getArguments().getParcelable("cardCollection");
        adapter = createCardAdapter(cardCollectionToShow);
        swipeStack.setAdapter(adapter);

        setHasOptionsMenu(true);
        addListeners();

        return root;
    }

    private void addListeners() {
        swipeStack.setListener(this);
        refreshCardsBtn.setOnClickListener(this);
        timerFab.setOnClickListener(this);
        cancelTimerBtn.setOnClickListener(this);

        // Stop bottom sheet from reacting on dragging so the timer works
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        // add Update fab button for timer
        circleTimerView.setCircleTimerListener(new CircleTimerView.CircleTimerListener() {
            @Override
            public void onTimerStop() {
                timerTxt.setText("---");
            }

            @Override
            public void onTimerStart(int time) {
                timerTxt.setText(time);
            }

            @Override
            public void onTimerPause(int time) {
                updateUIWithTimerText(circleTimerView.getCurrentTime());
            }

            @Override
            public void onTimerTimingValueChanged(int time) {
            }

            @Override
            public void onTimerSetValueChanged(int time) {
            }

            @Override
            public void onTimerSetValueChange(int time) {
                updateUIWithSaveIcon();
                if (time > 0) {
                    updateUIWithTimerText(TimeUnit.SECONDS.toMillis(time));
                }
            }
        });
    }

    private void updateUIWithSaveIcon() {
        timerFab.setImageDrawable(getResources().getDrawable(R.mipmap.ic_save));
    }

    private SwipeCardsAdapter createCardAdapter(final CardCollection collection) {
        List<String> mData = new ArrayList<String>();
        if(collection != null && (collection.getNotes() != null && !collection.getNotes().isEmpty())) {
            for(Note note : collection.getNotes()) {
                mData.add(note.getDescription() != null ? note.getDescription() : "no description");
            }
        } else {
            mData.add(getResources().getString(R.string.no_notes_in_collection));
        }

        return new SwipeCardsAdapter(mData, getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        if(presenter != null) {
            presenter.start();
        }
    }

    /**
     * Called when the fragment is no longer in use.  This is called after {@link #onStop()} and
     * before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Called when a view has been dismissed to the left.
     *
     * @param position The position of the view in the adapter currently in use.
     */
    @Override
    public void onViewSwipedToLeft(int position) {

    }

    /**
     * Called when a view has been dismissed to the right.
     *
     * @param position The position of the view in the adapter currently in use.
     */
    @Override
    public void onViewSwipedToRight(int position) {

    }

    /**
     * Called when the last view has been dismissed.
     */
    @Override
    public void onStackEmpty() {
        Toast.makeText(getActivity(), R.string.finished_pack, Toast.LENGTH_LONG).show();
        refreshCardsBtn.setVisibility(View.VISIBLE);
        swipeStack.setVisibility(View.GONE);

        bottomSheet.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyNoteError() {

    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showNotesAsCards(List<Note> notes) {

    }

    @Override
    public void showNoNotes() {

    }

    @Override
    public void showSuccessfullyLoadedNotes() {

    }

    @Override
    public void showNoTimerFound() {
        //TODO

    }

    @Override
    public void setPresenter(ReadNotesAsCardContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.refresh_cards_btn:
                refreshCardsBtn.setVisibility(View.GONE);
                swipeStack.setVisibility(View.VISIBLE);
                bottomSheet.setVisibility(View.VISIBLE);

                swipeStack.resetStack();
                break;
            case R.id.timer_fab:
                if (bottomSheet.getVisibility() == View.GONE) {
                    bottomSheet.setVisibility(View.VISIBLE);
                }
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
                    // to collapse
                    if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        cancelTimerBtn.setVisibility(View.VISIBLE);
                    } else {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                        // Start showing the timer in UI
                        saveReadingTime();
                    }
                    // Change icon
                    timerFab.setImageDrawable(getResources().getDrawable(R.mipmap.ic_timer));
                } else {
                    // to expand
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;
            case R.id.cancel_timer_btn:
                timerFab.setImageDrawable(getResources().getDrawable(R.mipmap.ic_timer));

                // Make default again
                clearUIOnCancel();
                stopCountdownTimer();
                break;
            default:
                break;
        }
    }

    private void clearUIOnCancel() {
        timerTxt.setText("---");
        circleTimerView.setCurrentTime(0);
    }

    private void stopCountdownTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    private void saveReadingTime() {
        circleTimerView.getCurrentTime();
        showTimerOnTextView(TimeUnit.SECONDS
                .toMillis(circleTimerView.getCurrentTime()), 1000);
    }

    private void updateUIWithTimerText(long miliseconds) {
        timerTxt.setText(String.format(Locale.ENGLISH, "%d:%02d", (int) (miliseconds / (1000 * 60)), (int) ((miliseconds / 1000) % 60)));
    }

    private void showTimerOnTextView(long duration, long interval) {
        timer = new CountDownTimer(duration, interval) {

            @Override
            public void onFinish() {
                // Update UI
                timerTxt.setText("---");
            }

            @Override
            public void onTick(long millisecondsLeft) {
                updateUIWithTimerText(millisecondsLeft);
            }
        };
        timer.start();
    }
}
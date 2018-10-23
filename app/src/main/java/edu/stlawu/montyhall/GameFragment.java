package edu.stlawu.montyhall;


import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;


public class    GameFragment extends Fragment {

    public GameFragment() {
        // Required empty public constructor
    }

    // instance variables
    private boolean inFirstPhase;
    private int selectedDoor = 0;
    private int winningDoor;
    private int goatDoor;
    private int chosenDoor;
    private ImageButton firstDoor;
    private ImageButton secondDoor;
    private ImageButton thirdDoor;
    private TextView prompt;
    private View switchButton;
    private View stayButton;
    private View newRoundButton;

    private Timer timer = null;
    private Counter counter = null;
    private int secondTracker = 4;

    private int winsSwitched = 0;
    private int lossesSwitched = 0;
    private int winsStayed = 0;
    private int lossesStayed = 0;
    private boolean stayed;
    private TextView valueTL;
    private TextView valueML;
    private TextView valueBL;
    private TextView valueTR;
    private TextView valueMR;
    private TextView valueBR;

    // Method used to toggle the visibility of a view on and off
    private void toggleVisibility(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    // Method used to handle the input from the doors
    private void doorManager(int doorNumber, ImageButton door) {
        firstDoor.setEnabled(false);
        secondDoor.setEnabled(false);
        thirdDoor.setEnabled(false);

        Animator anim = AnimatorInflater.loadAnimator((GameActivity)getActivity(), R.animator.shrink);
        anim.setTarget(door);
        anim.start();


        // change the prompt
        prompt.setText(R.string.stay_or_switch);
        // check to see what phase we're in
        if (inFirstPhase) {
            door.setImageResource(R.drawable.closed_door_chosen);
            // disable the doors and enable the buttons
            toggleVisibility(switchButton);
            toggleVisibility(stayButton);

            selectedDoor = doorNumber;


            // show the goat based on which door was picked
            goatDoor = ThreadLocalRandom.current().nextInt(1, 4);
            // keep randomizing goat door until a door is selected that is not the car door
            while (goatDoor == selectedDoor || goatDoor == winningDoor) {
                goatDoor = ThreadLocalRandom.current().nextInt(1, 4);
            }

            // set the goat door to the correct image
            if (goatDoor == 1) {
                firstDoor.setImageResource(R.drawable.goat);
            }
            if (goatDoor == 2) {
                secondDoor.setImageResource(R.drawable.goat);
            }
            if (goatDoor == 3) {
                thirdDoor.setImageResource(R.drawable.goat);
            }

            // change phases
            inFirstPhase = false;
        } else {
            // go back to the first phase
            inFirstPhase = true;

            timer = new Timer();
            counter = new Counter();
            timer.scheduleAtFixedRate(counter, 0, 1000);

            // change the prompt
            prompt.setText("");
        }
    }

    // Method used to determine if the player won or lost
    private void determineWinOrLoss(int selectedDoor, ImageButton door) {

        // did the player win?
        if (selectedDoor == winningDoor) {
            // you win!
            door.setImageResource(R.drawable.car);
            ((GameActivity)getActivity()).playSound(2);
            prompt.setText(R.string.you_win);

            // update the win count
            if (stayed) {
                winsStayed++;
            } else {
                winsSwitched++;
            }

        } else {
            // you lost.
            door.setImageResource(R.drawable.goat);
            ((GameActivity)getActivity()).playSound(1);
            prompt.setText(R.string.you_lose);

            // update the lose count
            if (stayed) {
                lossesStayed++;
            } else {
                lossesSwitched++;
            }
        }
        ((GameActivity)getActivity()).saveWinsLosses(winsStayed, lossesStayed, winsSwitched, lossesSwitched);
        // change the numbers on the UI
        updateWinsLosses();

        // give the player the option to start a new round
        newRoundButton.setVisibility(View.VISIBLE);
    }

    private void updateWinsLosses() {

        valueTL.setText(String.valueOf(winsStayed));
        valueML.setText(String.valueOf(lossesStayed));
        valueBL.setText(String.valueOf(winsStayed + lossesStayed));

        valueTR.setText(String.valueOf(winsSwitched));
        valueMR.setText(String.valueOf(lossesSwitched));
        valueBR.setText(String.valueOf(winsSwitched + lossesSwitched));

        // save the values
        //((GameActivity)getActivity()).saveWinsLosses(winsStayed, lossesStayed, winsSwitched, lossesSwitched);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // we are in the first phase
        inFirstPhase = true;
        // get a random door
        winningDoor = ThreadLocalRandom.current().nextInt(1, 4);

        // inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);

        // doors
        firstDoor = rootView.findViewById(R.id.door1);
        secondDoor = rootView.findViewById(R.id.door2);
        thirdDoor = rootView.findViewById(R.id.door3);
        // prompt
        prompt = rootView.findViewById(R.id.prompt);
        prompt.setText(R.string.choose_a_door);

        firstDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doorManager(1, firstDoor);
                ((GameActivity)getActivity()).playSound(3);
            }
        });
        secondDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doorManager(2, secondDoor);
                ((GameActivity)getActivity()).playSound(3);
            }
        });
        thirdDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doorManager(3, thirdDoor);
                ((GameActivity)getActivity()).playSound(3);
            }
        });

        // switch and stay buttons
        switchButton = rootView.findViewById(R.id.buttonSwitch);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleVisibility(switchButton);
                toggleVisibility(stayButton);

                // call the door manager with the other available door
                chosenDoor = ThreadLocalRandom.current().nextInt(1, 4);
                while (chosenDoor == goatDoor || chosenDoor == selectedDoor) {
                    chosenDoor = ThreadLocalRandom.current().nextInt(1, 4);
                }
                // reset the selected door image
                if (selectedDoor == 1) {
                    firstDoor.setImageResource(R.drawable.closed_door);
                }
                if (selectedDoor == 2) {
                    secondDoor.setImageResource(R.drawable.closed_door);
                }
                if (selectedDoor == 3) {
                    thirdDoor.setImageResource(R.drawable.closed_door);
                }

                if (chosenDoor == 1) {
                    doorManager(chosenDoor, firstDoor);
                }
                if (chosenDoor == 2) {
                    doorManager(chosenDoor, secondDoor);
                }
                if (chosenDoor == 3) {
                    doorManager(chosenDoor, thirdDoor);
                }
                // the player switched
                stayed = false;
            }
        });
        stayButton = rootView.findViewById(R.id.buttonStay);
        stayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleVisibility(switchButton);
                toggleVisibility(stayButton);
                chosenDoor = selectedDoor;
                // determine if this was the correct door
                if (selectedDoor == 1) {
                    doorManager(selectedDoor, firstDoor);
                }
                if (selectedDoor == 2) {
                    doorManager(selectedDoor, secondDoor);
                }
                if (selectedDoor == 3) {
                    doorManager(selectedDoor, thirdDoor);

                }

                // the player stayed
                stayed = true;
    }
        });

        newRoundButton = rootView.findViewById(R.id.buttonNewRound);
        newRoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // make the new round button invisible and set up a new round
                toggleVisibility(newRoundButton);
                ((GameActivity)getActivity()).playSound(0);
                firstDoor.setEnabled(true);
                secondDoor.setEnabled(true);
                thirdDoor.setEnabled(true);
                firstDoor.setImageResource(R.drawable.closed_door);
                secondDoor.setImageResource(R.drawable.closed_door);
                thirdDoor.setImageResource(R.drawable.closed_door);
                winningDoor = ThreadLocalRandom.current().nextInt(1, 4);
                prompt.setText(R.string.choose_a_door);
            }
        });

        // initially make the buttons invisible
        toggleVisibility(switchButton);
        toggleVisibility(stayButton);
        toggleVisibility(newRoundButton);

        // get the score count text views
        valueTL = rootView.findViewById(R.id.valueTL);
        valueML = rootView.findViewById(R.id.valueML);
        valueBL = rootView.findViewById(R.id.valueBL);
        valueTR = rootView.findViewById(R.id.valueTR);
        valueMR = rootView.findViewById(R.id.valueMR);
        valueBR = rootView.findViewById(R.id.valueBR);

        // was new or continue pressed?
        if (((GameActivity)getActivity()).getWinsLosses(4) == 1) {
            Log.e("T", "TRUE");
            this.winsStayed = ((GameActivity)getActivity()).getWinsLosses(0);
            this.lossesStayed = ((GameActivity)getActivity()).getWinsLosses(1);
            this.winsSwitched = ((GameActivity)getActivity()).getWinsLosses(2);
            this.lossesSwitched = ((GameActivity)getActivity()).getWinsLosses(3);
        } else {
            Log.e("F", "FALSE");
            winsStayed = 0;
            lossesStayed = 0;
            winsSwitched = 0;
            lossesSwitched = 0;
            ((GameActivity)getActivity()).saveWinsLosses(0, 0,0, 0);
        }
        ((GameActivity)getActivity()).isNoLongerNewGame();

        // and finally update the textViews
        updateWinsLosses();

        return rootView;
    }

    private void setDoorImage(ImageButton door) {
        if (secondTracker == 3) {
            door.setImageResource(R.drawable.three);
            ((GameActivity)getActivity()).playSound(4);
        }
        if (secondTracker == 2) {
            door.setImageResource(R.drawable.two);
            ((GameActivity)getActivity()).playSound(4);
        }
        if (secondTracker == 1) {
            door.setImageResource(R.drawable.one);
            ((GameActivity)getActivity()).playSound(4);

        }
        if (secondTracker <= 0) {
            // this click decides the final door
            determineWinOrLoss(chosenDoor, door);
            timer.cancel();
            timer.purge();
            secondTracker = 4;
        }
    }

    class Counter extends TimerTask {

        @Override
        public void run() {
            ((GameActivity)getActivity()).runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            // this receives an update on count every second
                            secondTracker--;

                            if (chosenDoor == 1) {
                                setDoorImage(firstDoor);
                            }
                            if (chosenDoor == 2) {
                                setDoorImage(secondDoor);
                            }
                            if (chosenDoor == 3) {
                                setDoorImage(thirdDoor);
                            }
                        }
                    }
            );
        }
    }

    @Override
    public void onStop() {
        ((GameActivity)getActivity()).saveWinsLosses(winsStayed, lossesStayed, winsSwitched, lossesSwitched);
        super.onStop();
    }
}

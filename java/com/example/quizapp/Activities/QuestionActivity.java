package com.example.quizapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.example.quizapp.Models.QuestionModel;
import com.example.quizapp.R;
import com.example.quizapp.databinding.ActivityQuestionBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {

        ArrayList<QuestionModel> list = new ArrayList<>();
        private int count = 0;
        private int position = 0;
        private int score = 0;
        CountDownTimer timer;
        ActivityQuestionBinding binding;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                binding = ActivityQuestionBinding.inflate(getLayoutInflater());
                setContentView(binding.getRoot());

                getSupportActionBar().hide();

                resetTimer();
                timer.start();

                String setName = getIntent().getStringExtra("set");

                if (setName.equals("Aptitude_Round")) {
                        setOne();
                } else if (setName.equals("Coding_Round")) {
                        setTwo();
                } else if (setName.equals("HR_Round")) {
                        setThree();
                }

                for (int i = 0; i < 4; i++) {
                        binding.optionContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                        checkAnswer((Button) view);

                                }

                        });
                }
                playAnimation(binding.question, 0, list.get(position).getQuestion());

                binding.btnNext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                                if (timer != null) {
                                        timer.cancel();
                                }
                                timer.start();
                                binding.btnNext.setEnabled(false);
                                binding.btnNext.setAlpha((float) 0.3);
                                enableOption(true);
                                position++;

                                if (position == list.size()) {

                                        Intent intent = new Intent(QuestionActivity.this, ScoreActivity.class);
                                        intent.putExtra("score", score);
                                        intent.putExtra("total", list.size());
                                        startActivity(intent);
                                        finish();
                                        return;
                                }

                                count = 0;
                                playAnimation(binding.question, 0, list.get(position).getQuestion());
                        }
                });
        }

        private void resetTimer() {

                timer = new CountDownTimer(30000, 1000) {
                        @Override
                        public void onTick(long l) {

                                binding.timer.setText(String.valueOf(l / 1000));
                        }

                        @Override
                        public void onFinish() {

                                Dialog dialog = new Dialog(QuestionActivity.this);
                                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.timeout_dialog);
                                dialog.findViewById(R.id.tryAgain).setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {

                                                Intent intent = new Intent(QuestionActivity.this, SetsActivity.class);
                                                startActivity(intent);
                                                finish();
                                        }
                                });

                                dialog.show();
                        }
                };
        }

        private void playAnimation(View view, int value, String data) {

                view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                                .setInterpolator(new DecelerateInterpolator())
                                .setListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(@NonNull Animator animation) {

                                                if (value == 0 && count < 4) {
                                                        String option = "";
                                                        if (count == 0) {

                                                                option = list.get(position).getOptionA();
                                                        } else if (count == 1) {

                                                                option = list.get(position).getOptionB();
                                                        } else if (count == 2) {
                                                                option = list.get(position).getOptionC();
                                                        } else if (count == 3) {
                                                                option = list.get(position).getOptionD();
                                                        }
                                                        playAnimation(binding.optionContainer.getChildAt(count), 0,
                                                                        option);
                                                        count++;
                                                }
                                        }

                                        @Override
                                        public void onAnimationEnd(@NonNull Animator animation) {
                                                if (value == 0) {

                                                        try {
                                                                ((TextView) view).setText(data);
                                                                binding.totalQuestion.setText(
                                                                                position + 1 + "/" + list.size());
                                                        } catch (Exception e) {
                                                                ((Button) view).setText(data);
                                                        }
                                                        view.setTag(data);
                                                        playAnimation(view, 1, data);
                                                }

                                        }

                                        @Override
                                        public void onAnimationCancel(@NonNull Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(@NonNull Animator animation) {

                                        }
                                });

        }

        private void enableOption(boolean enable) {
                for (int i = 0; i < 4; i++) {
                        binding.optionContainer.getChildAt(i).setEnabled(enable);
                        if (enable) {
                                binding.optionContainer.getChildAt(i).setBackgroundResource(R.drawable.btn_opt);
                        }
                }
        }

        private void checkAnswer(Button selectedOption) {

                if (timer != null) {
                        timer.cancel();
                }

                binding.btnNext.setEnabled(true);
                binding.btnNext.setAlpha(1);

                if (selectedOption.getText().equals(list.get(position).getCorrectAnswer())) {
                        score++;
                        selectedOption.setBackgroundResource(R.drawable.right_answ);
                } else {
                        selectedOption.setBackgroundResource(R.drawable.wrong_answ);
                        Button correctOption = (Button) binding.optionContainer
                                        .findViewWithTag(list.get(position).getCorrectAnswer());
                        correctOption.setBackgroundResource(R.drawable.right_answ);
                }
        }

        private void setThree() {
                list.add(new QuestionModel("How many ways can we solve this problem?\n" +
                                "A party is going on with “n” number of attendees, with only a single person known to every party attendee. This known person may be present at the party. However, this person doesn’t know anyone at the party, and we can only ask questions like, does A know B? How can you learn the stranger’s identity with the minimum number of questions asked? In how many ways can we solve this problem?",
                                "1.I can answer", "2.I'm not confident", "3.I don't know the answer", "4.None of these",
                                "1.I can answer"));
                list.add(new QuestionModel("Tell me about a time when you set and achieved a goal?",
                                "1.I can answer", "2.I'm not confident", "3.I don't know the answer", "4.None of these",
                                "1.I can answer"));
                list.add(new QuestionModel("How do you think digital Cyberwar will Rule in next five years!?",
                                "1.I can answer", "2.I'm not confident", "3.I don't know the answer", "4.None of these",
                                "1.I can answer"));
                list.add(new QuestionModel(
                                "Tell me about a time where you and a manager were in conflict. How did you ultimately resolve the problem?",
                                "1.I can answer", "2.I'm not confident", "3.I don't know the answer", "4.None of these",
                                "1.I can answer"));
                list.add(new QuestionModel(
                                "If I looked at your browser history right now, what would I learn about your personality?",
                                "1.I can answer", "2.I'm not confident", "3.I don't know the answer", "4.None of these",
                                "1.I can answer"));
                list.add(new QuestionModel("Sell me this pen !?",
                                "1.I can answer", "2.I'm not confident", "3.I don't know the answer", "4.None of these",
                                "1.I can answer"));
                list.add(new QuestionModel("Tell me something about yourself that you didn’t include on your resume",
                                "1.I can answer", "2.I'm not confident", "3.I don't know the answer", "4.None of these",
                                "1.I can answer"));
                list.add(new QuestionModel("Why do you want to work for our company?",
                                "1.I can answer", "2.I'm not confident", "3.I don't know the answer", "4.None of these",
                                "1.I can answer"));
                list.add(new QuestionModel("Why are you looking for a change?",
                                "1.I can answer", "2.I'm not confident", "3.I don't know the answer", "4.None of these",
                                "1.I can answer"));
                list.add(new QuestionModel("What is your biggest achievement so far?",
                                "1.I can answer", "2.I'm not confident", "3.I don't know the answer", "4.None of these",
                                "1.I can answer"));

        }

        private void setTwo() {

                list.add(new QuestionModel(
                                "1.Given a string \"Hello World\", what will be the output of the following code?\n" +
                                                "\n" +
                                                "s = \"Hello World\"\n" +
                                                "print(s[::-1][-1])",
                                "'d'", "'l'", "'o'", "'r'", "'l'"));
                list.add(new QuestionModel(
                                "2.Given an array [1, 2, 3, 4, 5, 4, 3, 2, 1], what will be the output of the following code?arr = [1, 2, 3, 4, 5, 4, 3, 2, 1]\n"
                                                +
                                                "result = 0\n" +
                                                "for num in arr:\n" +
                                                "    result ^= num\n" +
                                                "print(result)",
                                "1", "0", "2", "5", "0"));
                list.add(new QuestionModel("3.What is the time complexity of the following Fibonacci function?\n" +
                                "def fibonacci(n):\n" +
                                "    if n <= 1:\n" +
                                "        return n\n" +
                                "    return fibonacci(n - 1) + fibonacci(n - 2)",
                                "O(n)", "O(2^n)", "O(log n)", "O(n^2)", "O(2^n)"));
                list.add(new QuestionModel(
                                "4.Given a binary tree with in-order traversal [4, 2, 5, 1, 6, 3, 7], what will be the result of performing a post-order traversal?",
                                "[4, 5, 2, 6, 7, 3, 1]", "[4, 5, 6, 7, 2, 3, 1]", "[2, 5, 4, 6, 7, 3, 1]",
                                "[5, 4, 7, 6, 3, 2, 1]", "[4, 5, 2, 6, 7, 3, 1]"));
                list.add(new QuestionModel("5.Which of the following statements about a bipartite graph is true?",
                                " A bipartite graph must have an even number of nodes.",
                                "A bipartite graph can have cycles.", "A bipartite graph cannot have parallel edges.",
                                "A bipartite graph always has the same number of nodes in each partition.",
                                "A bipartite graph can have cycles."));
                list.add(new QuestionModel("6.What will be the output of the following code?\n" +
                                "    def _init_(self, value):\n" +
                                "        self.value = value\n" +
                                "        self.next = None\n" +
                                "def print_list(node):\n" +
                                "    while node:\n" +
                                "        print(node.value, end=' ')\n" +
                                "        node = node.next\n" +
                                "head = Node(1)\n" +
                                "head.next = Node(2)" +
                                "head.next.next = Node(3)" +
                                "print_list(head)",
                                "1 2 3", "1 3 2", "3 2 1", "3 1 2", "1 2 3"));
                list.add(new QuestionModel("7.What is the worst-case time complexity of the quicksort algorithm?",
                                "O(n)", "O(n log n)", "O(n^2)", "O(log n)", "O(n^2)"));
                list.add(new QuestionModel(
                                "8.Which of the following data structures follows the Last-In-First-Out (LIFO) principle?",
                                "Stack", "Queue", "Linked List", "Binary Search Tree", "Stack"));
                list.add(new QuestionModel(
                                "9.Given an array [3, 5, 2, 8, 1], what will be the output of the following code?\n" +
                                                "\n" +
                                                "hash_map = {}\n" +
                                                "for i in range(len(arr)):\n" +
                                                "    hash_map[arr[i]] = i\n" +
                                                "print(hash_map[8])",
                                "0", "1", "3", "4", "4"));
                list.add(new QuestionModel("10.Which of the following searching algorithms requires a sorted array?",
                                "Linear Search", "Binary Search", "Depth-First Search (DFS)",
                                "Breadth-First Search (BFS)", "Binary Search"));
                list.add(new QuestionModel(
                                "11.What is the output of the following code?List<Integer> numbers = new ArrayList<>();\n"
                                                +
                                                "numbers.add(1);\n" +
                                                "numbers.add(2);\n" +
                                                "numbers.add(3);\n" +
                                                "numbers.add(4);\n" +
                                                "\n" +
                                                "List<? extends Number> list = numbers;\n" +
                                                "list.add(5);\n" +
                                                "\n" +
                                                "System.out.println(list);",
                                "[1, 2, 3, 4]", "[1, 2, 3, 4, 5]", "Compilation Error", "Runtime exception",
                                "Compilation Error"));
                list.add(new QuestionModel("12.public class Main {\n" +
                                "    public static int sum(int n) {\n" +
                                "        if (n <= 0)\n" +
                                "            return 0;\n" +
                                "        return n + sum(n - 1);\n" +
                                "    }" +

                                "    public static void main(String[] args) {\n" +
                                "        System.out.println(sum(5));\n" +
                                "}\n" +
                                "What will be the output of the above code?",
                                "0", "5", "15", "25", "15"));
                list.add(new QuestionModel(
                                "13.Suppose the numbers 7, 5, 1, 8, 3, 6, 0, 9, 4, 2 are inserted in that order into an initially empty binary search tree. The binary search tree uses the usual ordering on natural numbers. What is the in-order traversal sequence of the resultant tree?",
                                "7 5 1 0 3 2 4 6 8 9", "0 2 4 3 1 6 5 9 8 7", "0 1 2 3 4 5 6 7 8 9",
                                "9 8 6 4 2 3 0 1 5 7", "0 1 2 3 4 5 6 7 8 9"));
                list.add(new QuestionModel(
                                "14.Consider two strings A = \"qpqrr\" and B = \"pqprqrp\". Let x be the length of the longest common subsequence (not necessarily contiguous) between A and B and let y be the number of such longest common subsequences between A and B. Then x + 10y = _.",
                                "33", "23", "43", "34", "34"));
                list.add(new QuestionModel(
                                "15.Let G be an undirected connected graph with distinct edge weight. Let emax be the edge with maximum weight and emin the edge with minimum weight. Which of the following statements is false? (GATE CS 2000)",
                                "Every minimum spanning tree of G must contain emin",
                                "If emax is in a minimum spanning tree, then its removal must disconnect G",
                                "No minimum spanning tree contains emax", "G has a unique minimum spanning tree",
                                "No minimum spanning tree contains emax"));
                list.add(new QuestionModel("16.#include <stdio.h>\n" +

                                "int main() {\n" +
                                "    int arr[] = {1, 2, 3, 4, 5};\n" +
                                "    int *p = &arr[2];\n" +
                                "    printf(\"%d\", *(p++));\n" +
                                "    return 0;\n" +
                                "}\n" +
                                "What will be the output of the above code snippet?",
                                "2", "3", "4", "5", "2"));
                list.add(new QuestionModel("17.public class MyClass {\n" +
                                "    public static void main(String[] args) {\n" +
                                "        String str = \"Hello, World!\";\n" +
                                "        System.out.println(str.substring(7, 12).length());\n" +
                                "    }\n" +
                                "}\n" +
                                " What will be the output" +
                                " of the above code snippet?",
                                "5", "6", "7", "12", "5"));
                list.add(new QuestionModel("How is the 3rd element in an array accessed based on pointer notation?",
                                "*a+3", "*(a+3)", "*(*a+3)", "&a+3", "*(a+3)"));
                list.add(new QuestionModel(
                                "The code above is a BMI (Body Mass Index) calculator written in Java. It takes the user's weight and height as input and calculates their BMI. What will be the output if a user enters a weight of 70 kg and a height of 1.75 meters?\n",
                                "Your BMI is: 140.0", "Your BMI is: 25.81", "Your BMI is: 28.06", "Your BMI is: 24.63",
                                "Your BMI is: 25.81"));
                list.add(new QuestionModel("20.function calculateAverage(numbers) {" +
                                "    let sum = 0;\n" +
                                "    for (let number of numbers) {\n" +
                                "        sum += number;\n" +
                                "    }" +
                                "    let average = sum / numbers.length;\n" +
                                "    return average;\n" +
                                "}" +
                                "let testScores = [85, 90, 92, 88, 95];\n" +
                                "let averageScore = calculateAverage(testScores);\n" +
                                "What will be the value of averageScore when the function is called with the testScores array as shown?\n",
                                "90", "91", "88", "89", "89"));
                list.add(new QuestionModel("21.public class TemperatureConverter {\n" +
                                "    public static double celsiusToFahrenheit(double celsius) {\n" +
                                "        double fahrenheit = (celsius * 9 / 5) + 32;\n" +
                                "        return fahrenheit;}" +
                                "    public static void main(String[] args) {\n" +
                                "        double celsiusTemperature = 25;\n" +
                                "        double fahrenheitTemperature = celsiusToFahrenheit(celsiusTemperature);\n" +
                                "        System.out.println(\"Fahrenheit: \" + fahrenheitTemperature);\n" +
                                "    }}" +
                                "output when celsiusTemperature = 25?\n",
                                "Fahrenheit: 54", "Fahrenheit: 45", "Fahrenheit: 77", "Fahrenheit: 25",
                                "Fahrenheit: 77"));
                list.add(new QuestionModel("22.def count_occurrences(string):\n" +
                                "    occurrence_dict = {}" +
                                "    for char in string:" +
                                "        if char in occurrence_dict:" +
                                "            occurrence_dict[char] += 1" +
                                "        else:" +
                                "            occurrence_dict[char] = 1" +
                                "    return occurrence_dict" +

                                "input_string = \"abracadabra\"" +
                                "result = count_occurrences(input_string)" +

                                "What will be the value of result when the function is called with input_string = \"abracadabra\"?",
                                "{'a': 5, 'b': 2, 'r': 2, 'c': 1, 'd': 1}", "{'a': 4, 'b': 2, 'r': 2, 'c': 1, 'd': 1}",
                                "{'a': 4, 'b': 2, 'r': 2, 'c': 2, 'd': 1}", "{'a': 5, 'b': 3, 'r': 2, 'c': 1, 'd': 1}",
                                "{'a': 5, 'b': 2, 'r': 2, 'c': 1, 'd': 1}"));
                list.add(new QuestionModel("23.def generate_fibonacci_sequence(n):\n" +
                                "    sequence = [0, 1]\n" +
                                "    for i in range(2, n):\n" +
                                "        sequence.append(sequence[i - 1] + sequence[i - 2])\n" +
                                "    return sequence\n" +
                                "fibonacci_sequence = generate_fibonacci_sequence(10)\n" +
                                "What will be the value of fibonacci_sequence when n = 10?",
                                " [0, 1, 1, 2, 3, 5, 8, 13, 21, 34]", " [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]",
                                " [0, 1, 1, 2, 3, 4, 5, 6, 7, 8]", "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                                " [0, 1, 1, 2, 3, 5, 8, 13, 21, 34]"));
                list.add(new QuestionModel("24.#include <iostream>\n" +

                                "int main() {" +
                                "    int a = 5;" +
                                "    int b = 7;" +

                                "    int result = (a > b) ? (a - b) : (b - a);\n" +

                                "    std::cout << \"The absolute difference between \" << a << \" and \" << b << \" is: \" << result << std::endl;\n"
                                +

                                "    return 0;\n" +
                                "}\n" +

                                "What will be the output ?",
                                "The absolute difference between 5 and 7 is: 2",
                                "The absolute difference between 7 and 5 is: 2",
                                "The absolute difference between 5 and 7 is: -2",
                                "The absolute difference between 7 and 5 is: -2",
                                "The absolute difference between 5 and 7 is: 2"));
                list.add(new QuestionModel("25.#include <stdio.h>\n" +
                                "void solve() {\n" +
                                "    int a = 3;\n" +
                                "    int res = a++ + ++a + a++ + ++a;\n" +
                                "    printf(\"%d\", res);\n" +
                                "}\n" +
                                "int main() {\n" +
                                "\tsolve();\n" +
                                "\treturn 0;\n" +
                                "}" + "what is output?",
                                "12", "24", "20", "18", "20"));

        }

        private void setOne() {
                list.add(new QuestionModel(
                                "The cost price of 20 articles is the same as the selling price of x articles. If the profit is 25%, then the value of x is",
                                "15", "16", "17", "18", "16"));

                list.add(new QuestionModel(
                                "In a certain store, the profit is 320% of the cost. If the cost increases by 25% but the selling price remains constant, approximately what percentage of the selling price is the profit?",
                                "30%", "50%", "60%", "70%", "70%"));
                list.add(new QuestionModel(
                                "Look at this series: 7, 10, 8, 11, 9, 12, ... What number should come next?",
                                "10", "13", "14", "6", "10"));
                list.add(new QuestionModel(
                                "The sum of ages of 5 children born at the intervals of 3 years each is 50 years. What is the age of the youngest child? ",
                                "6 years", "7 years", "8 years", "4 years", "4 years"));
                list.add(new QuestionModel(
                                "An error 2% in excess is made while measuring the side of a square. The percentage of error in the calculated area of the square is: ",
                                "2%", "4.04%", "2.2%", "4%", "4.04%"));
                list.add(new QuestionModel(
                                "One rabbit saw 6 elephants while going towards River. Every elephant saw 2 monkeys are going towards river. Every monkey holds one tortoice in their hands.\n"
                                                +
                                                "\n" +
                                                "How many animals are going towards the river?\n",
                                "14", "11", "8", "5", "5"));
                list.add(new QuestionModel(
                                "They come out at night without being called and are lost in the day without being stolen. What are they?",
                                "Light", "Bats", "Stars", "Flight", "Stars"));
                list.add(new QuestionModel(
                                "A clock is started at noon. By 10 minutes past 5, the hour hand has turned through:",
                                "145", "150", "155", "160", "155"));
                list.add(new QuestionModel(
                                "If A is substituted by 4, B by 3, C by 2, D by 4, E by 3, F by 2 and so on, then what will be total of the numerical values of the letters of the word SICK?",
                                "12", "11", "10", "9", "11"));
                list.add(new QuestionModel(
                                "A number of cats got together and decided to kill between them 999919 rats. Every cat killed an equal number of rats. Each cat killed more rats than there were cats. How many rats do you think that each cat killed ?",
                                "1009", "991", "2000", "1000", "1009"));

        }
}

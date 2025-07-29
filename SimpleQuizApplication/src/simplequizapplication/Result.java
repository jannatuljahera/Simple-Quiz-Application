/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simplequizapplication;

/**
 *
 * @author zubay
 */
public class Result {
        private final String username;
        private final int score;
        private final String date;

        public Result(String username, int score, String date) {
            this.username = username;
            this.score = score;
            this.date = date;
        }

        public String getUsername() {
            return username;
        }

        public int getScore() {
            return score;
        }

        public String getDate() {
            return date;
        }
    }

-- ==========================================
-- DATABASE CREATION SCRIPT: Minecraft Mob Battle
-- ==========================================

-- Create the database if it does not already exist
CREATE DATABASE IF NOT EXISTS minecraft_mob_battle;
USE minecraft_mob_battle;

-- Create the players table
-- Stores login credentials, gameplay stats, and leaderboard score
CREATE TABLE IF NOT EXISTS players (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    wins INT DEFAULT 0,
    losses INT DEFAULT 0,
    draws INT DEFAULT 0,
    score INT DEFAULT 0
);

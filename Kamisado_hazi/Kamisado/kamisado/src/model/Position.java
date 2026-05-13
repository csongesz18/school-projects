package model;

/**
 * Egy táblamező pozícióját (sor és oszlop) leíró egyszerű adattípus.
 * Tiszta és biztonságos módja koordináták együtt kezelésének.
 */
public record Position(int row, int col) {}

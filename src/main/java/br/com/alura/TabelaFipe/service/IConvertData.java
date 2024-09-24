package br.com.alura.TabelaFipe.service;

import java.util.List;

public interface IConvertData {

    <T> T gettingData(String json, Class<T> tClass);

    <T> List<T> getList(String json, Class<T> tClass);
}

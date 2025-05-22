package com.seumanoel.empacotador.service;

import com.seumanoel.empacotador.dto.EmpacotamentoResponse;
import com.seumanoel.empacotador.dto.CaixaEmpacotadaDTO;
import com.seumanoel.empacotador.model.Caixa;
import com.seumanoel.empacotador.model.Pedido;
import com.seumanoel.empacotador.model.Produto;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmpacotadorService {

    private final List<Caixa> caixasDisponiveis = List.of(
            new Caixa("Caixa 1", 30, 40, 80),
            new Caixa("Caixa 2", 80, 50, 40),
            new Caixa("Caixa 3", 50, 80, 60)
    );

    public List<EmpacotamentoResponse> empacotar(List<Pedido> pedidos) {
        List<EmpacotamentoResponse> resultado = new ArrayList<>();

        for (Pedido pedido : pedidos) {
            List<CaixaEmpacotadaDTO> caixasUsadas = new ArrayList<>();
            List<Produto> produtosRestantes = new ArrayList<>(pedido.getProdutos());

            produtosRestantes.sort((a, b) -> Double.compare(b.getVolume(), a.getVolume()));

            while (!produtosRestantes.isEmpty()) {
                Produto produtoAtual = produtosRestantes.remove(0);

                Optional<Caixa> caixaQueCabe = caixasDisponiveis.stream()
                        .filter(c -> cabe(c, produtoAtual))
                        .findFirst();

                if (caixaQueCabe.isPresent()) {
                    Caixa c = caixaQueCabe.get();
                    CaixaEmpacotadaDTO novaCaixa = new CaixaEmpacotadaDTO();
                    novaCaixa.tipo = c.getTipo();
                    novaCaixa.produtos = new ArrayList<>();
                    novaCaixa.produtos.add(produtoAtual);

                    caixasUsadas.add(novaCaixa);
                } else {
                    // Produto não cabe em nenhuma caixa
                    throw new RuntimeException("Produto " + produtoAtual.getNome() + " não cabe em nenhuma caixa.");
                }
            }

            EmpacotamentoResponse resp = new EmpacotamentoResponse();
            resp.pedidoId = pedido.getId();
            resp.caixas = caixasUsadas;

            resultado.add(resp);
        }

        return resultado;
    }

    private boolean cabe(Caixa c, Produto p) {
        return p.getAltura() <= c.getAltura() &&
                p.getLargura() <= c.getLargura() &&
                p.getComprimento() <= c.getComprimento();
    }
}

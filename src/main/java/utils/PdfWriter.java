package utils;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.util.List;

public class PdfWriter {
    public static void escreveTextoCentralizado(
            PDPageContentStream contentStream, PDPage page,
            String texto, float posicaoY)
            throws Exception {

        PDFont fonte = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        float tamanhoFonte = 24f;
        float larguraPagina = page.getMediaBox().getWidth();
        float larguraTexto = (fonte.getStringWidth(texto) / 1000) * tamanhoFonte;
        float inicioX = (larguraPagina - larguraTexto) / 2;

        contentStream.beginText();
        contentStream.setFont(fonte, tamanhoFonte);
        contentStream.newLineAtOffset(inicioX, posicaoY);
        contentStream.showText(texto);
        contentStream.endText();
    }
    public static void escreveTexto(
            PDPageContentStream contentStream, String texto, float posicaoY,
            float posicaoX)
            throws Exception {

        PDFont fonte = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        float tamanhoFonte = 16f;
        contentStream.beginText();
        contentStream.setFont(fonte, tamanhoFonte);
        contentStream.newLineAtOffset(posicaoX, posicaoY);
        contentStream.showText(texto);
        contentStream.endText();
    }

    public static void escreveTabela(List<List<String>> dados, PDPageContentStream contentStream,
                                     float posicaoY, float[] larguraColunas) throws Exception {

        final float margem = 50;
        final float alturaColuna = 20;
        final float LarguraTabela = 500;
        final float comecoYTabela = posicaoY;
        final float comecoXTabela = margem;
        final float margemCelula = 5;
        PDFont fonteCabecalho = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        PDFont fonteCorpo = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        float tamanhoFonte = 10f;

        float proximoY = comecoYTabela;
        float XAtual = comecoXTabela;

        for (int i = 0; i < dados.size(); i++) {

            PDFont currentFont = (i == 0) ? fonteCabecalho : fonteCorpo;
            contentStream.setFont(currentFont, tamanhoFonte);

            for (int j = 0; j < dados.get(i).size(); j++) {

                float cellWidth = larguraColunas[j] * LarguraTabela;

                contentStream.setLineWidth(1);
                contentStream.addRect(XAtual, proximoY - alturaColuna, cellWidth, alturaColuna);
                contentStream.stroke();

                contentStream.beginText();
                float textX = XAtual + margemCelula;
                float textY = proximoY - alturaColuna + (alturaColuna - tamanhoFonte) / 2;

                contentStream.newLineAtOffset(textX, textY);
                contentStream.showText(dados.get(i).get(j));
                contentStream.endText();

                // Move para a próxima coluna
                XAtual += cellWidth;
            }

            proximoY -= alturaColuna;
            XAtual = comecoXTabela;
        }
    }

}

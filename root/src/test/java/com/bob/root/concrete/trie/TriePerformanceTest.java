package com.bob.root.concrete.trie;

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.collection.AhoCorasick.AhoCorasickDoubleArrayTrie;
import com.hankcs.hanlp.collection.trie.DoubleArrayTrie;
import com.hankcs.hanlp.corpus.dictionary.DictionaryMaker;
import com.hankcs.hanlp.corpus.dictionary.item.Item;
import com.hankcs.hanlp.corpus.io.IOUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author wb-jjb318191
 * @create 2020-06-08 16:44
 */
public class TriePerformanceTest {

    @Test
    public void testCompareDatAndAcdatPerformance() {
        // 加载词典
        TreeMap<String, String> map = new TreeMap<>();
        for (Map.Entry<String, Item> entry : DictionaryMaker.load(HanLP.Config.CoreDictionaryPath).entrySet()) {
            map.put(entry.getKey(), entry.getKey());
        }
        // 加载母文本
        String text = IOUtil.readTxt("《我的团长我的团》.txt");
        final char charArray[] = text.toCharArray();
        long start;
        // 构建ACDAT
        AhoCorasickDoubleArrayTrie<String> acdat = new AhoCorasickDoubleArrayTrie<>();
        acdat.build(map);
        // 构建DAT
        DoubleArrayTrie<String> dat = new DoubleArrayTrie<>();
        dat.build(map);
        System.gc();

        // ACDAT测速
        start = System.currentTimeMillis();
        acdat.parseText(charArray, new AhoCorasickDoubleArrayTrie.IHit<String>() {
            @Override
            public void hit(int begin, int end, String value) {

            }
        });
        System.out.println("ACDAT: " + (System.currentTimeMillis() - start));

        DoubleArrayTrie<String>.Searcher searcher = dat.getSearcher(charArray, 0);
        System.gc();

        // DAT测速
        start = System.currentTimeMillis();
        while (searcher.next()) {
        }
        System.out.println("DAT: " + (System.currentTimeMillis() - start));

        // ACDAT和DAT等效性检测
        final TreeSet<String>[] wordNet = new TreeSet[charArray.length];
        acdat.parseText(charArray, new AhoCorasickDoubleArrayTrie.IHit<String>() {
            @Override
            public void hit(int begin, int end, String value) {
                if (wordNet[begin] == null) { wordNet[begin] = new TreeSet<>(); }
                wordNet[begin].add(new String(charArray, begin, end - begin));
            }
        });
        searcher = dat.getSearcher(charArray, 0);
        while (searcher.next()) {
            String word = new String(charArray, searcher.begin, searcher.length);
            assertEquals(true, wordNet[searcher.begin].contains(word));
            wordNet[searcher.begin].remove(word);
        }

        for (TreeSet<String> treeSet : wordNet) {
            if (treeSet == null) { continue; }
            assertEquals(0, treeSet.size());
        }
    }

}

package com.plato.recoserver.recoserver.core.rerank.strategy.inf;

import java.util.LinkedList;

/**
 * @author lishuguang
 * @date 2022/9/6
 **/
public interface IScatterWindows {
    
    
    
    
    class LimitLinkedSet<E> extends LinkedList<E> {
        private static final long serialVersionUID = 1L;
        private int length;

        public LimitLinkedSet(int length){
            this.length = length;
        }

        @Override
        public boolean add(E o){
            if(!super.contains(o)){
                super.add(o);
            }
            while (size() > length) {
                super.remove();
            }
            return true;
        }
    }
}

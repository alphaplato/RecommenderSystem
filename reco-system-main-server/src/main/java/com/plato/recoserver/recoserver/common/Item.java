package com.plato.recoserver.recoserver.common;

/**
 * 被推荐的物料，每个物料有一个唯一的id来标识
 * @author Kevin
 * @date 2022-03-14
 */
public interface Item<T> {

    /**
     * 此物料的唯一ID
     * @return 唯一id
     */
    T getId();

    /**
     * 内容的类型
     * 1-Article, 2-Product, 3-作品收藏夹, 4-单图收藏夹
     * 后续用枚举值替换
     * @return
     */
    int type();



    /**
     * 分数用于后续的计算，例如加权、降权
     * @return 分数
     */
    double getScore();
}

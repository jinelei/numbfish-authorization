package com.jinelei.iotgenius.helper;

import java.util.*;

/**
 * @version v1.0.0
 * <br/>
 * 有向无环图 检查 <br/>
 * boolean hasCycle = new GraphHelper().addNode(1L, 2L) <br/>
 * .addNode(2L, 3L) <br/>
 * .addNode(3L, 4L) <br/>
 * .addNode(4L, 1L) <br/>
 * .hasCycle(); <br/>
 * System.out.println(hasCycle); <br/>
 * </p>
 * @description:
 * @author: jinelei
 * @create: 2024/4/30
 **/
@SuppressWarnings("unused")
public class GraphHelper {
    protected final Map<Long, Set<Long>> dependenceMap = new HashMap<>();

    /**
     * 添加节点
     *
     * @param id            当前节点id
     * @param dependenceIds 依赖节点ids
     * @return Graph
     */
    public GraphHelper addNode(Long id, Long... dependenceIds) {
        Set<Long> dependenceList = this.dependenceMap.getOrDefault(id, new HashSet<>());
        dependenceList.addAll(List.of(dependenceIds));
        this.dependenceMap.put(id, dependenceList);
        return this;
    }

    /**
     * 检查途中是否存在环
     *
     * @return 是否存在环
     */
    public boolean hasCycle() {
        Set<Long> visited = new HashSet<>();
        Set<Long> recurs = new HashSet<>();
        for (Long node : dependenceMap.keySet()) {
            if (hasCycleSearch(node, visited, recurs)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否存在环
     *
     * @param node      当前节点
     * @param visited   已访问节点
     * @param recursion 递归的集合
     * @return 是否存在
     */
    protected boolean hasCycleSearch(Long node, Set<Long> visited, Set<Long> recursion) {
        Optional.ofNullable(node).orElseThrow();
        Optional.ofNullable(visited).orElseThrow();
        Optional.ofNullable(recursion).orElseThrow();
        if (recursion.contains(node)) {
            // 发现了环
            return true;
        }
        if (visited.contains(node)) {
            // 已经访问过，不是环的一部分
            return false;
        }
        visited.add(node);
        recursion.add(node);
        for (Long neighbor : this.dependenceMap.getOrDefault(node, new HashSet<>())) {
            if (hasCycleSearch(neighbor, visited, recursion)) {
                return true;
            }
        }
        // 回溯，离开递归
        recursion.remove(node);
        return false;
    }
}

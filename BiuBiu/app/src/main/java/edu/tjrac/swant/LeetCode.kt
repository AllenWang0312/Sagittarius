package edu.tjrac.swant

/**
 * Created by wpc on 2019-10-30.
 */
class LeetCode {
    fun twoSum(nums: IntArray, target: Int): IntArray {
        var arr = IntArray(2)
        for (i in 0 until nums.size) {
            for (j in i until nums.size) {
                if (nums[i] + nums[j] == target) {

                    arr[0] = i
                    arr[1] = j
                    return arr
                }
            }
        }
        return arr
    }
}

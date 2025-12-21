package day09

import common.arrays.swap
import java.util.Collections
import kotlin.collections.filterIsInstance
import kotlin.collections.toTypedArray
import kotlin.collections.withIndex

/**
 * Day 9: Disk Fragmenter
 *
 * https://adventofcode.com/2024/day/9
 */
class Day09 {
  class Part1 {
    fun solve(input: String): Long {
      val blocks: List<Block> = parseDiskMap(input)
      val moved = moveFileBlocks(blocks)
      return checkSum(moved)
    }

    private fun moveFileBlocks(blocks: List<Block>): List<Block> {
      val rearranged = blocks.toTypedArray()
      var left = rearranged.indexOfFirst { it is Block.FreeSpace }
      var right = rearranged.indexOfLast { it is Block.File }
      while (left < right) {
        when {
          rearranged[left] is Block.File -> left++
          rearranged[right] is Block.FreeSpace -> right--
          else -> rearranged.swap(left, right)
        }
      }
      return rearranged.toList()
    }

    private fun parseDiskMap(diskMap: String): List<Block> = buildList {
      var fileId = 0
      for ((index, digitChar) in diskMap.withIndex()) {
        val size = digitChar.digitToInt()
        if (index % 2 == 0) {
          val id = fileId++
          addAll(Collections.nCopies(size, Block.File(id)))
        } else {
          addAll(Collections.nCopies(size, Block.FreeSpace))
        }
      }
    }
  }

  class Part2 {
    fun solve(input: String): Long {
      val segments: List<DiskSegment> = parseDiskMap(input)
      val blocks: Array<Block> = getBlocks(segments)
      moveBlocks(segments, blocks)
      return checkSum(blocks.toList())
    }

    private fun moveBlocks(segments: List<DiskSegment>, blocks: Array<Block>) {
      val fileSegments: List<DiskSegment.File> =
        segments.filterIsInstance<DiskSegment.File>().sortedByDescending { it.id }

      for (fileSegment in fileSegments) {
        attemptToMoveFile(blocks, fileSegment)
      }
    }

    private fun attemptToMoveFile(blocks: Array<Block>, fileSegment: DiskSegment.File) {
      val fileId = fileSegment.id
      var left = blocks.indexOfFirst { it is Block.FreeSpace }
      var right = blocks.indexOfFirst { (it as? Block.File)?.id == fileId }
      while (left < right) {
        when {
          (blocks[right] as? Block.File)?.id == fileId &&
            (blocks[right - 1] as? Block.File)?.id == fileId -> right--
          blocks[left] is Block.File -> left++
          blocks[right] is Block.FreeSpace -> right--
          !blocks.canFit(fileSegment, startIndex = left) -> left++
          // Move block
          else -> {
            for (i in 0 until fileSegment.size) {
              blocks[right + i] = Block.FreeSpace
              blocks[left + i] = Block.File(fileId)
            }
            return
          }
        }
      }
    }

    private fun Array<Block>.canFit(file: DiskSegment.File, startIndex: Int): Boolean {
      val indexRange = startIndex..<(startIndex + file.size)
      return indexRange.all { i -> this[i] == Block.FreeSpace }
    }

    private fun getBlocks(segments: List<DiskSegment>): Array<Block> {
      val numBlocks = getRequiredSize(segments)
      val blocks: Array<Block> = Array(size = numBlocks) { Block.FreeSpace }
      var blockIndex = 0
      for (segment in segments) {
        when (segment) {
          is DiskSegment.FreeSpace -> blockIndex += segment.size
          is DiskSegment.File -> {
            repeat(segment.size) { blocks[blockIndex++] = Block.File(id = segment.id) }
          }
        }
      }

      return blocks
    }

    private fun getRequiredSize(segments: List<DiskSegment>): Int =
      segments.sumOf { segment -> segment.size }

    sealed class DiskSegment {
      abstract val size: Int

      data class File(override val size: Int, val id: Int) : DiskSegment()

      data class FreeSpace(override val size: Int) : DiskSegment()
    }

    private fun parseDiskMap(diskMap: String): List<DiskSegment> = buildList {
      var fileId = 0
      for ((index, digitChar) in diskMap.withIndex()) {
        val size = digitChar.digitToInt()
        if (index % 2 == 0) {
          val id = fileId++
          add(DiskSegment.File(size = size, id = id))
        } else {
          add(DiskSegment.FreeSpace(size = size))
        }
      }
    }
  }
}

sealed class Block {
  data class File(val id: Int) : Block()

  data object FreeSpace : Block()
}

private fun checkSum(blocks: List<Block>): Long =
  blocks.withIndex().sumOf { (index, block) ->
    when (block) {
      is Block.FreeSpace -> 0L
      is Block.File -> block.id * index.toLong()
    }
  }

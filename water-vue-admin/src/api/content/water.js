import request from '@/utils/request'
import downloadService from '@/utils/downloadService'

// 查询水质列表所有的数据
export function listWater(query) {
  return request({
    url: '/content/water/list',
    method: 'get',
    params: query
  })
}

// // 查询分类列表
// export function listAllBanner() {
//   return request({
//     url: '/content/banner/listAllBanner',
//     method: 'get'
//   })
// }
// 查询水质详细
export function getWater(id) {
  return request({
    url: '/content/water/' + id,
    method: 'get'
  })
}
// 查询水质检测数据的最新数据
export function getNewWater() {
  return request({
    url: '/content/water/new',
    method: 'get'
  })
}

// 新增水质
export function addWater(data) {
  return request({
    url: '/content/water',
    method: 'post',
    data: data
  })
}

// 修改水质
export function updateWater(data) {
  return request({
    url: '/content/water',
    method: 'put',
    data: data
  })
}

// 删除水质
export function delWater(id) {
  return request({
    url: '/content/water/' + id,
    method: 'delete'
  })
}


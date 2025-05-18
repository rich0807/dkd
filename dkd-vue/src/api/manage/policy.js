import request from '@/utils/request'

// 查询策略管理列表
export function listPolicy(query) {
  return request({
    url: '/manage/policy/list',
    method: 'get',
    params: query
  })
}

// 查询策略管理详细
export function getPolicy(policyId) {
  return request({
    url: '/manage/policy/' + policyId,
    method: 'get'
  })
}

// 新增策略管理
export function addPolicy(data) {
  return request({
    url: '/manage/policy',
    method: 'post',
    data: data
  })
}

// 修改策略管理
export function updatePolicy(data) {
  return request({
    url: '/manage/policy',
    method: 'put',
    data: data
  })
}

// 删除策略管理
export function delPolicy(policyId) {
  return request({
    url: '/manage/policy/' + policyId,
    method: 'delete'
  })
}

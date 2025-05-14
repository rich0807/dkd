<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="设备编号" prop="innerCode">
        <el-input
          v-model="queryParams.innerCode"
          placeholder="请输入设备编号"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['manage:vm:add']"
        >新增</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="vmList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="设备编号" align="center" prop="innerCode" />
      <el-table-column label="设备型号" align="center" prop="innerCode">
        <template #default="scope">
        <div v-for="item in vmTypeList" :key="item.id">
        <span v-if="item.id == scope.row.vmTypeId">{{item.name}}</span>
  
          
        </div>
        </template>
      </el-table-column>
      <el-table-column label="详细地址" align="center" prop="addr" />
      <el-table-column label="合作商名称" align="center" prop="partnerId">
        <template #default="scope">
        <div v-for="item in partnerList" :key="item.id">
        <span v-if="item.id == scope.row.partnerId">{{item.partnerName}}</span> 
        </div>
        </template>
      </el-table-column>
    
      <el-table-column label="设备状态" align="center" prop="vmStatus">
        <template #default="scope">
          <dict-tag :options="vm_status" :value="scope.row.vmStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary"  @click="handleUpdate(scope.row)" v-hasPermi="['manage:vm:edit']">修改</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

<!-- 添加或修改设备管理对话框 -->
<el-dialog :title="title" v-model="open" width="500px" append-to-body>
  <el-form ref="vmRef" :model="form" :rules="rules" label-width="80px">
    <el-form-item label="设备编号">
      <span>{{ form.innerCode == null ? '系统自动生成' : form.innerCode }}</span>
    </el-form-item>
    <el-form-item label="供货时间" v-if="form.innerCode != null">
      <span>{{ parseTime(form.lastSupplyTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
    </el-form-item>
    <el-form-item label="设备类型" v-if="form.innerCode != null">
      <div v-for="item in vmTypeList" :key="item.id">
        <span v-if="form.vmTypeId == item.id">{{ item.name }}</span>
      </div>
    </el-form-item>
    <el-form-item label="设备容量" v-if="form.innerCode != null">
      <span>{{ form.channelMaxCapacity }}</span>
    </el-form-item>
    <el-form-item label="选择型号" prop="vmTypeId" v-if="form.innerCode == null">
      <!-- <el-input v-model="form.vmTypeId" placeholder="请输入设备型号" /> -->
      <el-select v-model="form.vmTypeId" placeholder="请选择设备型号" style="width: 100%">
        <el-option v-for="item in vmTypeList" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
    </el-form-item>
    <el-form-item label="选择点位" prop="nodeId">
      <!-- <el-input v-model="form.nodeId" placeholder="请输入点位Id" /> -->
      <el-select v-model="form.nodeId" placeholder="请选择点位" style="width: 100%">
        <el-option v-for="item in nodeList" :key="item.id" :label="item.nodeName" :value="item.id" />
      </el-select>
    </el-form-item>
    <el-form-item label="合作商" v-if="form.innerCode != null">
      <div v-for="item in partnerList" :key="item.id">
        <span v-if="form.partnerId == item.id">{{ item.partnerName }}</span>
      </div>
    </el-form-item>
    <el-form-item label="所属区域" v-if="form.innerCode != null">
      <div v-for="item in regionList" :key="item.id">
        <span v-if="form.regionId == item.id">{{ item.regionName }}</span>
      </div>
    </el-form-item>
    <el-form-item label="设备地址" v-if="form.innerCode != null">
        <span>{{ form.addr }}</span>
    </el-form-item>
  </el-form>
  <template #footer>
    <div class="dialog-footer">
      <el-button type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </template>
</el-dialog>
  </div>


</template>

<script setup name="Vm">
import { listVm, getVm, delVm, addVm, updateVm } from "@/api/manage/vm";
import{listVmType} from "@/api/manage/vmType";
import{ listPartner} from "@/api/manage/partner";
import {loadAllParams} from "@/api/page";
import {listNode} from "@/api/manage/node";
import { listRegion } from "@/api/manage/region";
const { proxy } = getCurrentInstance();
const { vm_status } = proxy.useDict('vm_status');

const vmList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    innerCode: null,
    nodeId: null,
    businessType: null,
    regionId: null,
    partnerId: null,
    vmTypeId: null,
    vmStatus: null,
    runningStatus: null,
    policyId: null,
  },
  rules: {
    nodeId: [
      { required: true, message: "点位Id不能为空", trigger: "blur" }
    ],
    vmTypeId: [
      { required: true, message: "设备型号不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询设备管理列表 */
function getList() {
  loading.value = true;
  listVm(queryParams.value).then(response => {
    vmList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    innerCode: null,
    channelMaxCapacity: null,
    nodeId: null,
    addr: null,
    lastSupplyTime: null,
    businessType: null,
    regionId: null,
    partnerId: null,
    vmTypeId: null,
    vmStatus: null,
    runningStatus: null,
    longitudes: null,
    latitude: null,
    clientId: null,
    policyId: null,
    createTime: null,
    updateTime: null
  };
  proxy.resetForm("vmRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加设备管理";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getVm(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改设备管理";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["vmRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateVm(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addVm(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除设备管理编号为"' + _ids + '"的数据项？').then(function() {
    return delVm(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('manage/vm/export', {
    ...queryParams.value
  }, `vm_${new Date().getTime()}.xlsx`)
}

// 查询设备列表
const vmTypeList = ref([]);
function getVmTypeList() {
  listVmType(loadAllParams).then(response => {
    vmTypeList.value = response.rows;
  });
}
// 查询合作商列表
const partnerList = ref([]);
function getPartnerList() {
  listPartner(loadAllParams).then(response => {
    partnerList.value = response.rows;
  });
}

// 查询点位列表
const nodeList = ref([]);
function getNodeList() {
  listNode(loadAllParams).then(response => {
    nodeList.value = response.rows;
  });
}

/* 查询区域列表 */
const regionList = ref([]);
function getRegionList() {
  listRegion(loadAllParams).then((response) => {
    regionList.value = response.rows;
  });
}
getRegionList();

getNodeList();
getVmTypeList();
getPartnerList();
getList();
</script>

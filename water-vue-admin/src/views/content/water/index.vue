<template>
  <div class="app-container">


    <el-row :gutter="20" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple"
          @click="handleDelete">删除</el-button>
      </el-col>

    </el-row>

    <el-table v-loading="loading" :data="waterList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="酸性值" align="center" prop="ph" />
      <el-table-column label="纯度" align="center" prop="purity" />
      <el-table-column label="温度" align="center" prop="temp" />
      <el-table-column label="预警值" align="center" prop="warning" />
      <el-table-column label="时间" align="center" prop="createTime" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination :page-size.sync="queryParams.pageSize" layout="total, sizes, prev, pager, next, jumper"
      :total="total" :page-sizes="[10, 20, 30, 40]" :current-page.sync="queryParams.pageNum" @current-change="getList"
      @size-change="getList" />

    <!-- 添加或修改分类对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="酸性值" prop="ph">
          <el-input-number v-model="form.ph" :precision="2" :step="0.1" :min="0.01" :max="10" />
        </el-form-item>
        <el-form-item label="纯净度" prop="purity">
          <el-input-number v-model="form.purity" :precision="2" :step="0.1" :min="0.01" :max="100" />
        </el-form-item>
         <el-form-item label="温度" prop="temp">
          <el-input-number v-model="form.temp" :precision="1" :step="0.1" :min="0.01" :max="100" />
        </el-form-item>
        <el-form-item label="预警值" prop="warning">
          <el-input-number v-model="form.warning" :min="1" :max="100" />
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    listWater,
    getWater,
    delWater,
    addWater,
    updateWater
  } from '@/api/content/water'


  export default {
    name: 'Water',
    data() {
      return {
        // 遮罩层
        loading: true,
        // 导出遮罩层
        exportLoading: false,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // 轮播图表格数据
        waterList: null,
        // 弹出层标题
        title: '',
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: null,
          metaKeywords: null,
          metaDescription: null,
          status: undefined,
          delFlag: 0
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {},
      }
    },
    created() {
      this.getList()
    },
    methods: {
      /** 查询水质列表 */
      getList() {
        this.loading = true
        listWater(this.queryParams).then(response => {
          this.waterList = response.rows
          this.total = response.total
          this.loading = false
        })
      },
      // 取消按钮
      cancel() {
        this.open = false
        this.reset()

      },
      // 表单重置
      reset() {
        this.form = {
          id: null,
          ph:null,
          purity:null,
          temp:null,
          waring:null

        }
        this.resetForm('form')
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1
        this.getList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm('queryForm')
        this.handleQuery()
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.id)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset()
        this.open = true
        this.title = '添加水质监测数据'
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        const id = row.id || this.ids
        getWater(id).then(response => {

          this.form = response
          this.open = true
          this.title = '添加水质监测数据'
        })
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs['form'].validate(valid => {
          if (valid) {
            if (this.form.id != null) {


              updateWater(this.form).then(response => {

                this.$modal.msgSuccess('修改成功')
                this.open = false
                this.getList()
              })
            } else {
              addWater(this.form).then(response => {

                this.$modal.msgSuccess('新增成功')
                this.open = false
                this.getList()
              })
            }
          }
        })
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const ids = row.id || this.ids
        this.$modal.confirm('是否确认删除水质编号为"' + ids + '"的数据项？').then(function() {
          return delWater(ids)
        }).then(() => {
          this.getList()
          this.$modal.msgSuccess('删除成功')
        }).catch(() => {})
      }
    }
  }
</script>

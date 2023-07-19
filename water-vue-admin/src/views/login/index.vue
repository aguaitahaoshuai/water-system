<template>
  <div class="login-container">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on"
      label-position="left" v-show="isLogin">
      <div class="title-container">
        <h3 class="title">水质监测后台管理</h3>
      </div>

      <el-form-item prop="userName">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input ref="userName" v-model="loginForm.userName" placeholder="Username" name="userName" type="text"
          tabindex="1" auto-complete="on" />
      </el-form-item>

      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input :key="passwordType" ref="password" v-model="loginForm.password" :type="passwordType"
          placeholder="Password" name="password" tabindex="2" auto-complete="on" @keyup.enter.native="handleLogin" />
        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
      </el-form-item>

      <el-button :loading="loginLoading" type="primary" style="width: 100%; margin-bottom: 30px"
        @click.native.prevent="handleLogin">登录</el-button>


      <div class="tips">
        <span style="margin-right: 20px">userName: admin</span>
        <span> password: 1234</span>
      </div>

    </el-form>


  </div>
</template>

<script>
  import {
    validUsername
  } from '@/utils/validate'
  import {
    listUser,
    getUser,
    delUser,
    addUser,
    updateUser,
    changeUserStatus
  }
  from '@/api/system/user'
  import {
    listAllRole
  }
  from '@/api/system/role'

  export default {
    name: 'Login',
    data() {
      const validateUsername = (rule, value, callback) => {
        if (!validUsername(value)) {
          callback(new Error('Please enter the correct user name'))
        } else {
          callback()
        }
      }
      const validatePassword = (rule, value, callback) => {
        if (value.length < 1) {
          callback(new Error('The password can not be less than 6 digits'))
        } else {
          callback()
        }
      }
      return {
        loginForm: {
          userName: 'admin',
          password: '1234'
        },
        loginRules: {
          userName: [{
            required: true,
            trigger: 'blur',
            validator: validateUsername
          }],
          password: [{
            required: true,
            trigger: 'blur',
            validator: validatePassword
          }]
        },
        loginLoading: false,
        registerLoading: false,
        passwordType: 'password',
        redirect: undefined,
        // 表单校验
        rules: {
          userName: [{
              required: true,
              message: '用户名称不能为空',
              trigger: 'blur'
            },
            {
              min: 2,
              max: 20,
              message: '用户名称长度必须介于 2 和 20 之间',
              trigger: 'blur'
            }
          ],
          nickName: [{
            required: true,
            message: '用户昵称不能为空',
            trigger: 'blur'
          }],
          password: [{
              required: true,
              message: '用户密码不能为空',
              trigger: 'blur'
            },
            {
              min: 5,
              max: 20,
              message: '用户密码长度必须介于 5 和 20 之间',
              trigger: 'blur'
            }
          ],
          email: [{
            type: 'email',
            message: "'请输入正确的邮箱地址",
            trigger: ['blur', 'change']
          }],
          phonenumber: [{
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: '请输入正确的手机号码',
            trigger: 'blur'
          }]
        },
        // 角色选项
        roleOptions: [],
        // 显示搜索条件
        showSearch: true,
        // 遮罩层
        loading: true,
        // 用户表格数据
        userList: null,
        // 总条数
        total: 0,
        // 选中数组
        ids: [],
        // 表单参数
        form: {},
        isLogin: true
      }
    },
    watch: {
      $route: {
        handler: function(route) {
          this.redirect = route.query && route.query.redirect
        },
        immediate: true
      }
    },
    mounted() {
      /** 获取用户角色 */
      listAllRole().then((response) => {
        this.roleOptions = response
        console.log(this.roleOptions);
      })
    },
    methods: {
      handleRegister() {

      },
      showPwd() {
        if (this.passwordType === 'password') {
          this.passwordType = ''
        } else {
          this.passwordType = 'password'
        }
        this.$nextTick(() => {
          this.$refs.password.focus()
        })
      },
      handleLogin() {
        this.$refs.loginForm.validate((valid) => {
          if (valid) {
            this.loading = true
            this.$store
              .dispatch('Login', this.loginForm)
              .then(() => {
                this.$router.push({
                  path: this.redirect || '/'
                })
                this.loading = false
              })
              .catch(() => {
                this.loading = false
              })
          } else {
            console.log('error submit!!')
            return false
          }
        })
      },

      // 取消按钮
      cancel() {

        this.reset()
      },
      // 表单重置
      reset() {
        this.form = {
          id: undefined,
          userName: undefined,
          nickName: undefined,
          password: undefined,
          phonenumber: undefined,
          email: undefined,
          sex: undefined,
          status: '0',
          remark: undefined,
          roleIds: []
        }
        this.resetForm('form')
      },
      /** 提交按钮 */
      handleRegister: function() {
        this.$refs['form'].validate((valid) => {
          if (valid) {
            addUser(this.form).then((response) => {
              this.$modal.msgSuccess('注册成功')
              this.reset();
              this.isLogin = true;

            })
          }
        })
      }
    }
  }
</script>

<style lang="scss">
  /* 修复input 背景不协调 和光标变色 */
  /* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

  $bg: #283443;
  $light_gray: #fff;
  $cursor: #fff;

  @supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
    .login-container .el-input input {
      color: $cursor;
    }
  }

  /* reset element-ui css */
  .login-container {
    .el-input {
      display: inline-block;
      height: 47px;
      width: 85%;

      input {
        background: transparent;
        border: 0px;
        -webkit-appearance: none;
        border-radius: 0px;
        padding: 12px 5px 12px 15px;
        color: $light_gray;
        height: 47px;
        caret-color: $cursor;

        &:-webkit-autofill {
          box-shadow: 0 0 0px 1000px $bg inset !important;
          -webkit-text-fill-color: $cursor !important;
        }
      }
    }

    .el-form-item {
      border: 1px solid rgba(255, 255, 255, 0.1);
      background: rgba(0, 0, 0, 0.1);
      border-radius: 5px;
      color: #454545;
    }
    .el-form-item:focus{
      border-color: #2fb0f5;
    }

    .el-select {
      width: 100%;
    }
  }
</style>

<style lang="scss" scoped>
  $bg: #2d3a4b;
  $dark_gray: #67C23A;
  $light_gray: #eee;

  #register:hover {
    color: #0055ff;
    cursor: pointer;
  }


  .login-container {
    min-height: 100%;
    width: 100%;
     background-color: $bg;
    //background: url('../../assets/images/bgo.jpg') no-repeat;
    background-size: 100%;
    overflow: hidden;
    display: flex;
    justify-content: center;
    align-items: center;


    .login-form {
      position: relative;
      width: 520px;
      max-width: 100%;
      padding: 50px 35px;
      margin: 0 auto;
      overflow: hidden;
      background-color: white;
      margin: 0 auto;
      border: 1px solid #EEE;
      border-radius: 5px;
      box-shadow: 0 20px 40px 0 rgba(0, 0, 0, .08);

    }

    .tips {
      font-size: 14px;
      // color: #fff;
      margin-bottom: 10px;
      margin-top: 5px;
      color: #333;

      span {
        &:first-of-type {
          margin-right: 16px;
        }

        transition: .6s all;
      }
    }

    .svg-container {
      padding: 6px 5px 6px 15px;
      color: $dark_gray;
      vertical-align: middle;
      width: 30px;
      display: inline-block;
    }

    .title-container {
      position: relative;

      .title {
        font-size: 26px;
        color: #F56C6C;
        margin: 0px auto 40px auto;
        text-align: center;
        font-weight: bold;
      }
    }

    .show-pwd {
      position: absolute;
      right: 10px;
      top: 7px;
      font-size: 16px;
      color: $dark_gray;
      cursor: pointer;
      user-select: none;
    }
  }
</style>

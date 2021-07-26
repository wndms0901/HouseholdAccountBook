<template>
  <transition name="modal" appear>
    <div class="modal modal-overlay">
      <div class="modal-window">
        <div class="modal-content">
          <slot />
        </div>
        <footer class="modal-footer">
          <slot name="footer">
            <button @click="$emit('close')">Close</button>
          </slot>
        </footer>
      </div>
    </div>
  </transition>
</template>

<style lang="scss" scoped>
.modal {
  &.modal-overlay {
    left: 1000px;
    top: 123px;
    display: block;
    position: fixed;
    z-index: 30;
    width: 375px;
    height: 160px;
    border-radius: 5px;
    font-size: 14px;
    color: #424242;
  }

  &-window {
    background: #fff;
    border: 1px solid #babfc7;
    border-radius: 4px;
    overflow: hidden;
  }

  &-content {
    padding: 10px 20px;
    border: 0;
    text-align: center;
  }

  &-footer {
    // background: #ccc;
    padding: 10px 20px;
  }
}

// 오버레이 트랜지션
.modal-enter-active,
.modal-leave-active {
  transition: opacity 0.4s;

  // 오버레이에 포함되어 있는 모달 윈도의 트랜지션
  .modal-window {
    transition: opacity 0.4s, transform 0.4s;
  }
}

// 딜레이가 적용된 모달 윈도가 제거된 후에 오버레이가 사라짐
.modal-leave-active {
  transition: opacity 0.6s ease 0.4s;
}

.modal-enter,
.modal-leave-to {
  opacity: 0;

  .modal-window {
    opacity: 0;
    transform: translateY(-20px);
  }
}
</style>
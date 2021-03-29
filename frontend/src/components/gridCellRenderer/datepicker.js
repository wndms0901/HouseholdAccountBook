// import Vue from 'vue';
// import { AgGridVue } from 'ag-grid-vue';
// import 'ag-grid-community/dist/styles/ag-grid.css';
// import 'ag-grid-community/dist/styles/ag-theme-alpine.css';

// const VueExample = {
//     template: `
//         <div style="height: 100%">
//             <ag-grid-vue
//                 style="width: 100%; height: 100%;"
//                 class="ag-theme-alpine"
//                 id="myGrid"
//                 :gridOptions="gridOptions"
//                 @grid-ready="onGridReady"
//                 :columnDefs="columnDefs"
//                 :defaultColDef="defaultColDef"
//                 :components="components"
//                 :rowData="rowData"></ag-grid-vue>
//         </div>
//     `,
//     components: {
//         'ag-grid-vue': AgGridVue,
//     },
//     data: function () {
//         return {
//             gridOptions: null,
//             gridApi: null,
//             columnApi: null,
//             columnDefs: null,
//             defaultColDef: null,
//             components: null,
//             rowData: null,
//         };
//     },
//     beforeMount() {
//         this.gridOptions = {};
//         this.columnDefs = [
//             { field: 'athlete' },
//             {
//                 field: 'date',
//                 editable: true,
//                 cellEditor: 'datePicker',
//             },
//             {
//                 field: 'age',
//                 maxWidth: 110,
//             },
//             { field: 'country' },
//             {
//                 field: 'year',
//                 maxWidth: 120,
//             },
//             { field: 'sport' },
//             { field: 'gold' },
//             { field: 'silver' },
//             { field: 'bronze' },
//             { field: 'total' },
//         ];
//         this.defaultColDef = {
//             flex: 1,
//             minWidth: 150,
//         };
//         this.components = { datePicker: getDatePicker() };
//     },
//     mounted() {
//         this.gridApi = this.gridOptions.api;
//         this.gridColumnApi = this.gridOptions.columnApi;
//     },
//     methods: {
//         onGridReady(params) {
//             const updateData = (data) => {
//                 this.rowData = data;
//             };

//             fetch('https://www.ag-grid.com/example-assets/olympic-winners.json')
//                 .then((resp) => resp.json())
//                 .then((data) => updateData(data));
//         },
//     },
// };

window.getDatePicker = function getDatePicker() {
    function Datepicker() { }
    Datepicker.prototype.init = function (params) {
        this.eInput = document.createElement('input');
        this.eInput.value = params.value;
        this.eInput.classList.add('ag-input');
        this.eInput.style.height = '100%';
        $(this.eInput).datepicker({ dateFormat: 'dd/mm/yy' });
    };
    Datepicker.prototype.getGui = function () {
        return this.eInput;
    };
    Datepicker.prototype.afterGuiAttached = function () {
        this.eInput.focus();
        this.eInput.select();
    };
    Datepicker.prototype.getValue = function () {
        return this.eInput.value;
    };
    Datepicker.prototype.destroy = function () { };
    Datepicker.prototype.isPopup = function () {
        return false;
    };
    return Datepicker;
};

// new Vue({
//     el: '#app',
//     components: {
//         'my-component': VueExample,
//     },
// });
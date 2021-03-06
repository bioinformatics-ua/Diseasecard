import { configureStore } from '@reduxjs/toolkit';
import templateReducer from '../template/templateSlice';
import addSourceSlice from '../features/sourcesManagement/addSource/addSourceSlice';
import querySystemSlice from '../features/sparqlEndpoint/querySystem/querySystemSlice';
import listResourceSlice from '../features/sourcesManagement/listResources/listResourcesSlice';
import systemStatusSlice from "../features/sourcesManagement/systemStatus/systemStatusSlice";
import listPrefixesSlice from "../features/sparqlEndpoint/listPrefixes/listPrefixesSlice";
import endpointManagementSlice from "../features/endpointManagement/endpointManagementSlice";
import alertBoxSlice from "../features/endpointAlertBox/alertBoxSlice";
import { getDefaultMiddleware } from '@reduxjs/toolkit'
import { reducer as formReducer } from 'redux-form';
import dashboardSlice from "../features/dashboard/dashboardSlice";
import sourceMapSlice from "../features/sourcesMap/sourceMapSlice";


const customizedMiddleware = getDefaultMiddleware({
  serializableCheck: false
})

export default configureStore({
  reducer: {
    template: templateReducer,
    addSource: addSourceSlice,
    dashboard: dashboardSlice,
    querySystem: querySystemSlice,
    listResources: listResourceSlice,
    systemStatus: systemStatusSlice,
    form: formReducer,
    listPrefixes: listPrefixesSlice,
    endpointManagement: endpointManagementSlice,
    alertBox: alertBoxSlice,
    sourceMap: sourceMapSlice
  },
  middleware: customizedMiddleware
});

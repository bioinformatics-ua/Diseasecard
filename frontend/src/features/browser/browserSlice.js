import { createAsyncThunk, createSlice } from '@reduxjs/toolkit'
import API from '../../api/Api'


const initialState = {
    results:[],
    letter: "",
    status: 'idle',
    error: null
}

export const getResults = createAsyncThunk('browse/getResults', async (letter) => {
    return API.GET("browserResults", "", [letter] ).then(res => {
        // TODO: fazer processamento
        console.log(res.data)
        return res.data
    })
})


const browserSlice = createSlice({
    name: 'browser',
    initialState,
    reducers: {},
    extraReducers: {
        [getResults.pending]: (state, action) => {
            state.status = 'loading'
            state.letter = action.meta.arg
        },
        [getResults.fulfilled]: (state, action) => {
            state.status = 'succeeded'
            state.results = action.payload
            state.letter = action.meta.arg
        },
        [getResults.rejected]: (state, action) => {
            state.status = 'failed'
            state.letter = action.meta.arg
            state.error = action.error.message
        },
    }
})

export default browserSlice.reducer
import React, { useState } from 'react';
import { useDispatch } from "react-redux";
import { Container, Form, InputGroup } from 'react-bootstrap'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import {faAlignJustify, faSearch} from '@fortawesome/free-solid-svg-icons'
import { getResults, getAutocomplete } from "./searchSlice";
import { Link, useHistory } from "react-router-dom";
import { AsyncTypeahead, Token } from 'react-bootstrap-typeahead';
import { unwrapResult } from "@reduxjs/toolkit";
import { showFrame } from "../disease/diseaseSlice";

import logo from "../../images/logo.png"

export const SearchForm = () => {
    const [searchInput, setSearchInput] = useState('')
    const [isLoading, setIsLoading] = useState(false);
    const [options, setOptions] = useState([]);
    const props = {};
    const dispatch = useDispatch();
    const history = useHistory();

    props.multiple = true;
    props.renderToken = (option, {onRemove}, index) => (
        <Token key={option.omim} onRemove={onRemove} option={option} > {`${option.info} (OMIM: ${option.omim})`} </Token>
    )


    const onSearchButtonClicked = () => {
        if (searchInput) {
            dispatch(showFrame("graph"))
            dispatch(getResults(searchInput))
            setSearchInput('')
        }
    }

    const handleSearchAutocomplete = async query => {
        setIsLoading(true);
        setSearchInput(query);
        const results = await dispatch(getAutocomplete(query))
        setOptions(unwrapResult(results))
        setIsLoading(false);
    };

    const handleSelectedOption = ( selected ) => {
        dispatch(showFrame("graph"))
        history.push('/disease/' + selected[0].omim)
    }

    return (
        <Container id="index">
            <div id="logo">
                <a href="#about" title="About DiseaseCard" >
                    <img className="logo img-responsive" width="434" height="59" src={ logo} alt="" />
                </a>
            </div>
            <Form id="search_form">
                <Form.Group>
                    <Form.Label htmlFor="inlineSearch" srOnly>
                        Search
                    </Form.Label>
                    <InputGroup value={ searchInput }>
                        <AsyncTypeahead
                            {...props}
                            id="async_search"
                            isLoading={isLoading}
                            labelKey={(option) => `${option.info}`}
                            minLength={4}
                            onSearch={ handleSearchAutocomplete }
                            onChange={ handleSelectedOption }
                            options={options}
                            placeholder="Search..."
                        />
                        <InputGroup.Append>
                            <Link to={'/searchResults'} onClick={ onSearchButtonClicked } className="btn btn-primary"><FontAwesomeIcon icon={ faSearch }/></Link>
                        </InputGroup.Append>
                    </InputGroup>
                </Form.Group>
            </Form>
            <div style={{ marginTop: "50px" }}>
                <p>Welcome to <b style={{fontWeight: "bold"}}>Diseasecard</b>!</p>
                <p> Try the system by entering the name of a disease in the search field above <b style={{fontWeight: "bold"}}>or</b> navigating through our
                    Browser (accessible from the icon <FontAwesomeIcon style={{fontSize: "13px"}} icon={faAlignJustify}/>  in the upper right corner).
                </p>
                <br/>
                <p>
                    We are currently reshaping DiseaseCard, so some features and sources will be gradually updated.<br />
                    Thank you for your patience.
                </p>
            </div>
        </Container>
    );
}
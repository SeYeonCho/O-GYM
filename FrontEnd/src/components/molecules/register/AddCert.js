import React from "react";
import { useState, useEffect } from "react";
import { useRecoilState } from "recoil";
import { AddCERT } from "../../../recoil/atoms/AddCERT";
import styled from "styled-components";
import ReactDOM from "react-dom";
import "antd/dist/antd.css";
import { Button, DatePicker } from "antd";

const StyledInput = styled.input`
  display: block;
  box-sizing: border-box;
  width: 100%;
  border-radius: 4px;
  border: 1px solid black;
  padding: 10px 10px;
  margin-bottom: 10px;
  font-size: 14px;
  color: black;
`;

function AddCert() {
  const [form, setForm] = useState([]);
  const [certInfo, setcertInfo] = useRecoilState(AddCERT);

  const prevIsValid = () => {
    if (form.length === 0) {
      return true;
    }

    const someEmpty = form.some(
      (item) => item.publisher === "" || item.date === "" || item.name === ""
    );

    if (someEmpty) {
      form.map((item, index) => {
        const allPrev = [...form];

        if (form[index].name === "") {
          allPrev[index].errors.name = "name is required";
        }

        if (form[index].date === "") {
          allPrev[index].errors.date = "date is required";
        }

        if (form[index].publisher === "") {
          allPrev[index].errors.publisher = "publisher is required";
        }
        setForm(allPrev);
      });
    }

    return !someEmpty;
  };

  const handleAddLink = (e) => {
    e.preventDefault();
    const inputState = {
      name: "",
      date: "",
      publisher: "",

      errors: {
        name: null,
        date: null,
        publisher: null,
      },
    };

    if (prevIsValid()) {
      setForm((prev) => [...prev, inputState]);
    }
    // const newForm = form.map((item, index) => {
    //   return {
    //     platform: item["SNSPlatform"],
    //     SNSUrl: item["SNSUrl"],
    //   };
    // });
    // console.log(newForm);
    // console.log(form);
  };

  const onChange = (index, event) => {
    event.preventDefault();
    event.persist();

    setForm((prev) => {
      return prev.map((item, i) => {
        if (i !== index) {
          return item;
        }

        return {
          ...item,
          [event.target.name]: event.target.value,

          errors: {
            ...item.errors,
            [event.target.name]:
              event.target.value.length > 0
                ? null
                : [event.target.name] + " Is required",
          },
        };
      });
    });
    // setsnsInfo(form);
  };

  useEffect(() => {
    const newForm = form.map((item, index) => {
      return {
        name: item["name"],
        date: item["date"],
        publisher: item["publisher"],
      };
    });
    setcertInfo(newForm);
    // console.log(certInfo);
  }, [form]);

  const handleRemoveField = (e, index) => {
    e.preventDefault();

    setForm((prev) => prev.filter((item) => item !== prev[index]));
  };
  return (
    <>
      <div>
        <form>
          {form.map((item, index) => (
            <div key={`item-${index}`} style={{ display: "flex" }}>
              <div>
                <StyledInput
                  type="text"
                  name="name"
                  placeholder="자격증 이름"
                  value={item.name}
                  onChange={(e) => onChange(index, e)}
                />

                {item.errors.name && (
                  <div style={{ color: "red" }}>{item.errors.name}</div>
                )}
              </div>
              <div>
                <StyledInput
                  type="date"
                  name="date"
                  placeholder="취득일자"
                  value={item.date}
                  onChange={(e) => onChange(index, e)}
                />

                {item.errors.date && (
                  <div style={{ color: "red" }}>{item.errors.date}</div>
                )}
              </div>
              <div>
                <StyledInput
                  type="text"
                  name="publisher"
                  placeholder="발행기관"
                  value={item.publisher}
                  onChange={(e) => onChange(index, e)}
                />

                {item.errors.publisher && (
                  <div style={{ color: "red" }}>{item.errors.publisher}</div>
                )}
              </div>

              <Button
                type="primary"
                danger
                onClick={(e) => handleRemoveField(e, index)}
              >
                X
              </Button>
            </div>
          ))}

          <Button type="primary" onClick={handleAddLink}>
            자격증 추가
          </Button>
        </form>
      </div>
    </>
  );
}

export default AddCert;

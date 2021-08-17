import React from "react";
import { useState, useEffect } from "react";
import { useRecoilState } from "recoil";
import { AddCAREER } from "../../../recoil/atoms/AddCAREER";
import styled from "styled-components";
import ReactDOM from "react-dom";
import "antd/dist/antd.css";
import { Button, DatePicker, Row, Col } from "antd";

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
  const [careerInfo, setcareerInfo] = useRecoilState(AddCAREER);

  const prevIsValid = () => {
    if (form.length === 0) {
      return true;
    }

    const someEmpty = form.some(
      (item) =>
        item.enddate === "" ||
        item.startdate === "" ||
        item.role === "" ||
        item.company === ""
    );

    if (someEmpty) {
      form.map((item, index) => {
        const allPrev = [...form];

        if (form[index].company === "") {
          allPrev[index].errors.company = "company is required";
        }

        if (form[index].role === "") {
          allPrev[index].errors.role = "role is required";
        }

        if (form[index].startdate === "") {
          allPrev[index].errors.startdate = "startdate is required";
        }

        if (form[index].enddate === "") {
          allPrev[index].errors.enddate = "enddate is required";
        }

        setForm(allPrev);
      });
    }

    return !someEmpty;
  };

  const handleAddLink = (e) => {
    e.preventDefault();
    const inputState = {
      company: "",
      role: "",
      startdate: "",
      enddate: "",

      errors: {
        company: null,
        role: null,
        startdate: null,
        enddate: null,
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
        company: item["company"],
        role: item["role"],
        startdate: item["startdate"],
        enddate: item["enddate"],
      };
    });
    setcareerInfo(newForm);
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
            <Row key={`item-${index}`} style={{ display: "flex" }}>
              <Col span={4}>
                <StyledInput
                  type="text"
                  name="company"
                  placeholder="회사명"
                  value={item.company}
                  onChange={(e) => onChange(index, e)}
                />

                {item.errors.company && (
                  <div style={{ color: "red" }}>{item.errors.company}</div>
                )}
              </Col>
              <Col span={3}>
                <StyledInput
                  type="text"
                  name="role"
                  placeholder="직책"
                  value={item.role}
                  onChange={(e) => onChange(index, e)}
                />

                {item.errors.role && (
                  <div style={{ color: "red" }}>{item.errors.role}</div>
                )}
              </Col>

              <Col span={7}>
                <StyledInput
                  type="date"
                  name="startdate"
                  date-placeholder="시작일자"
                  value={item.startdate}
                  onChange={(e) => onChange(index, e)}
                />

                {item.errors.startdate && (
                  <div style={{ color: "red" }}>{item.errors.startdate}</div>
                )}
              </Col>
              <Col span={7}>
                <StyledInput
                  type="date"
                  name="enddate"
                  date-placeholder="종료일자"
                  value={item.enddate}
                  onChange={(e) => onChange(index, e)}
                  style={{}}
                />

                {item.errors.enddate && (
                  <div style={{ color: "red" }}>{item.errors.enddate}</div>
                )}
              </Col>
              <Col span={1}>
                <Button
                  type="primary"
                  danger
                  onClick={(e) => handleRemoveField(e, index)}
                >
                  X
                </Button>
              </Col>
            </Row>
          ))}

          <Button type="primary" onClick={handleAddLink}>
            커리어 추가
          </Button>
        </form>
      </div>
    </>
  );
}

export default AddCert;

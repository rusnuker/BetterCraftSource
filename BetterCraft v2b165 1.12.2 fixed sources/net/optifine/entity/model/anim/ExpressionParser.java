// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.entity.model.anim;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import optifine.Config;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Deque;
import java.io.IOException;
import java.util.Collection;
import java.util.ArrayDeque;
import java.util.Arrays;

public class ExpressionParser
{
    private IModelResolver modelResolver;
    
    public ExpressionParser(final IModelResolver modelResolver) {
        this.modelResolver = modelResolver;
    }
    
    public IExpression parse(final String str) throws ParseException {
        try {
            final Token[] atoken = TokenParser.parse(str);
            if (atoken == null) {
                return null;
            }
            final Deque<Token> deque = new ArrayDeque<Token>(Arrays.asList(atoken));
            return this.parseInfix(deque);
        }
        catch (final IOException ioexception) {
            throw new ParseException(ioexception.getMessage(), ioexception);
        }
    }
    
    private IExpression parseInfix(final Deque<Token> deque) throws ParseException {
        if (deque.isEmpty()) {
            return null;
        }
        final List<IExpression> list = new LinkedList<IExpression>();
        final List<Token> list2 = new LinkedList<Token>();
        final IExpression iexpression = this.parseExpression(deque);
        checkNull(iexpression, "Missing expression");
        list.add(iexpression);
        while (true) {
            final Token token = deque.poll();
            if (token == null) {
                return this.makeInfix(list, list2);
            }
            if (token.getType() != EnumTokenType.OPERATOR) {
                throw new ParseException("Invalid operator: " + token);
            }
            final IExpression iexpression2 = this.parseExpression(deque);
            checkNull(iexpression2, "Missing expression");
            list2.add(token);
            list.add(iexpression2);
        }
    }
    
    private IExpression makeInfix(final List<IExpression> listExpr, final List<Token> listOper) throws ParseException {
        final List<EnumFunctionType> list = new LinkedList<EnumFunctionType>();
        for (final Token token : listOper) {
            final EnumFunctionType enumfunctiontype = EnumFunctionType.parse(token.getText());
            checkNull(enumfunctiontype, "Invalid operator: " + token);
            list.add(enumfunctiontype);
        }
        return this.makeInfixFunc(listExpr, list);
    }
    
    private IExpression makeInfixFunc(final List<IExpression> listExpr, final List<EnumFunctionType> listFunc) throws ParseException {
        if (listExpr.size() != listFunc.size() + 1) {
            throw new ParseException("Invalid infix expression, expressions: " + listExpr.size() + ", operators: " + listFunc.size());
        }
        if (listExpr.size() == 1) {
            return listExpr.get(0);
        }
        int i = Integer.MAX_VALUE;
        int j = Integer.MIN_VALUE;
        for (final EnumFunctionType enumfunctiontype : listFunc) {
            i = Math.min(enumfunctiontype.getPrecedence(), i);
            j = Math.max(enumfunctiontype.getPrecedence(), j);
        }
        if (j < i || j - i > 10) {
            throw new ParseException("Invalid infix precedence, min: " + i + ", max: " + j);
        }
        for (int k = j; k >= i; --k) {
            this.mergeOperators(listExpr, listFunc, k);
        }
        if (listExpr.size() == 1 && listFunc.size() == 0) {
            return listExpr.get(0);
        }
        throw new ParseException("Error merging operators, expressions: " + listExpr.size() + ", operators: " + listFunc.size());
    }
    
    private void mergeOperators(final List<IExpression> listExpr, final List<EnumFunctionType> listFuncs, final int precedence) {
        for (int i = 0; i < listFuncs.size(); ++i) {
            final EnumFunctionType enumfunctiontype = listFuncs.get(i);
            if (enumfunctiontype.getPrecedence() == precedence) {
                listFuncs.remove(i);
                final IExpression iexpression = listExpr.remove(i);
                final IExpression iexpression2 = listExpr.remove(i);
                final IExpression iexpression3 = new Function(enumfunctiontype, new IExpression[] { iexpression, iexpression2 });
                listExpr.add(i, iexpression3);
                --i;
            }
        }
    }
    
    private IExpression parseExpression(final Deque<Token> deque) throws ParseException {
        final Token token = deque.poll();
        checkNull(token, "Missing expression");
        switch (token.getType()) {
            case CONSTANT: {
                return makeConstant(token);
            }
            case IDENTIFIER: {
                final EnumFunctionType enumfunctiontype = this.getFunctionType(token, deque);
                if (enumfunctiontype != null) {
                    return this.makeFunction(enumfunctiontype, deque);
                }
                return this.makeVariable(token);
            }
            case BRACKET_OPEN: {
                return this.makeBracketed(token, deque);
            }
            case OPERATOR: {
                final EnumFunctionType enumfunctiontype2 = EnumFunctionType.parse(token.getText());
                checkNull(enumfunctiontype2, "Invalid operator: " + token);
                if (enumfunctiontype2 == EnumFunctionType.PLUS) {
                    return this.parseExpression(deque);
                }
                if (enumfunctiontype2 == EnumFunctionType.MINUS) {
                    final IExpression iexpression = this.parseExpression(deque);
                    return new Function(EnumFunctionType.NEG, new IExpression[] { iexpression });
                }
                break;
            }
        }
        throw new ParseException("Invalid expression: " + token);
    }
    
    private static IExpression makeConstant(final Token token) throws ParseException {
        final float f = Config.parseFloat(token.getText(), Float.NaN);
        if (f == Float.NaN) {
            throw new ParseException("Invalid float value: " + token);
        }
        return new Constant(f);
    }
    
    private EnumFunctionType getFunctionType(final Token token, final Deque<Token> deque) throws ParseException {
        final Token token2 = deque.peek();
        if (token2 != null && token2.getType() == EnumTokenType.BRACKET_OPEN) {
            final EnumFunctionType enumfunctiontype1 = EnumFunctionType.parse(token2.getText());
            checkNull(enumfunctiontype1, "Unknown function: " + token2);
            return enumfunctiontype1;
        }
        final EnumFunctionType enumfunctiontype2 = EnumFunctionType.parse(token2.getText());
        if (enumfunctiontype2 == null) {
            return null;
        }
        if (enumfunctiontype2.getCountArguments() > 0) {
            throw new ParseException("Missing arguments: " + enumfunctiontype2);
        }
        return enumfunctiontype2;
    }
    
    private IExpression makeFunction(final EnumFunctionType type, final Deque<Token> deque) throws ParseException {
        if (type.getCountArguments() == 0) {
            return makeFunction(type, new IExpression[0]);
        }
        final Token token = deque.poll();
        final Deque<Token> deque2 = getGroup(deque, EnumTokenType.BRACKET_CLOSE, true);
        final IExpression[] aiexpression = this.parseExpressions(deque2);
        return makeFunction(type, aiexpression);
    }
    
    private IExpression[] parseExpressions(final Deque<Token> deque) throws ParseException {
        final List<IExpression> list = new ArrayList<IExpression>();
        while (true) {
            final Deque<Token> deque2 = getGroup(deque, EnumTokenType.COMMA, false);
            final IExpression iexpression = this.parseInfix(deque2);
            if (iexpression == null) {
                break;
            }
            list.add(iexpression);
        }
        final IExpression[] aiexpression = list.toArray(new IExpression[list.size()]);
        return aiexpression;
    }
    
    private static IExpression makeFunction(final EnumFunctionType type, final IExpression[] exprs) throws ParseException {
        if (type.getCountArguments() != exprs.length) {
            throw new ParseException("Invalid number of arguments: " + exprs.length + ", should be: " + type.getCountArguments() + ", function: " + type.getName());
        }
        return new Function(type, exprs);
    }
    
    private IExpression makeVariable(final Token token) throws ParseException {
        if (this.modelResolver == null) {
            throw new ParseException("Model variable not found: " + token);
        }
        final IExpression iexpression = this.modelResolver.getExpression(token.getText());
        if (iexpression == null) {
            throw new ParseException("Model variable not found: " + token);
        }
        return iexpression;
    }
    
    private IExpression makeBracketed(final Token token, final Deque<Token> deque) throws ParseException {
        final Deque<Token> deque2 = getGroup(deque, EnumTokenType.BRACKET_CLOSE, true);
        return this.parseInfix(deque2);
    }
    
    private static Deque<Token> getGroup(final Deque<Token> deque, final EnumTokenType tokenTypeEnd, final boolean tokenEndRequired) throws ParseException {
        final Deque<Token> deque2 = new ArrayDeque<Token>();
        int i = 0;
        final Iterator iterator = deque2.iterator();
        while (iterator.hasNext()) {
            final Token token = iterator.next();
            iterator.remove();
            if (i == 0 && token.getType() == tokenTypeEnd) {
                return deque2;
            }
            deque2.add(token);
            if (token.getType() == EnumTokenType.BRACKET_OPEN) {
                ++i;
            }
            if (token.getType() != EnumTokenType.BRACKET_CLOSE) {
                continue;
            }
            --i;
        }
        if (tokenEndRequired) {
            throw new ParseException("Missing end token: " + tokenTypeEnd);
        }
        return deque2;
    }
    
    private static void checkNull(final Object obj, final String message) throws ParseException {
        if (obj == null) {
            throw new ParseException(message);
        }
    }
    
    public static void main(final String[] args) throws Exception {
        final ExpressionParser expressionparser = new ExpressionParser(null);
    Label_0009_Outer:
        while (true) {
            while (true) {
                try {
                    while (true) {
                        final InputStreamReader inputstreamreader = new InputStreamReader(System.in);
                        final BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
                        final String s = bufferedreader.readLine();
                        if (s.length() <= 0) {
                            break;
                        }
                        final IExpression iexpression = expressionparser.parse(s);
                        final float f = iexpression.eval();
                        Config.dbg(s + " = " + f);
                    }
                    return;
                }
                catch (final Exception exception) {
                    exception.printStackTrace();
                    continue Label_0009_Outer;
                }
                continue;
            }
        }
    }
}
